package dev.anvilcraft.lib.v2.rendering.optimization.occlusion.hiz;

import com.mojang.blaze3d.buffers.GpuBuffer;
import com.mojang.blaze3d.buffers.GpuFence;
import com.mojang.blaze3d.pipeline.RenderTarget;
import com.mojang.blaze3d.systems.CommandEncoder;
import com.mojang.blaze3d.systems.GpuDevice;
import com.mojang.blaze3d.textures.GpuTexture;
import dev.anvilcraft.lib.v2.rendering.ALRComputePipelines;
import dev.anvilcraft.lib.v2.rendering.extension.blaze3d.ALRCommandEncoderExtension;
import dev.anvilcraft.lib.v2.rendering.extension.blaze3d.ALRGpuDeviceExtension;
import dev.anvilcraft.lib.v2.rendering.extension.blaze3d.MemoryBarrierFlag;
import dev.anvilcraft.lib.v2.rendering.extension.blaze3d.compute.pipeline.ALRComputePass;
import dev.anvilcraft.lib.v2.rendering.foundation.buffers.GpuBufferConstants;
import dev.anvilcraft.lib.v2.rendering.foundation.buffers.StagingSupport;
import dev.anvilcraft.lib.v2.rendering.foundation.buffers.layout.BufferLayout;
import dev.anvilcraft.lib.v2.rendering.optimization.occlusion.CullingStatistics;
import dev.anvilcraft.lib.v2.rendering.optimization.occlusion.OcclusionCuller;
import dev.anvilcraft.lib.v2.rendering.optimization.occlusion.OcclusionKey;
import dev.anvilcraft.lib.v2.rendering.optimization.occlusion.hiz.buffers.Aabb;
import dev.anvilcraft.lib.v2.rendering.optimization.occlusion.hiz.buffers.OcclusionTestCB;
import dev.anvilcraft.lib.v2.rendering.optimization.occlusion.hiz.buffers.OcclusionTestSSBO;
import dev.anvilcraft.lib.v2.rendering.optimization.occlusion.hiz.converter.DepthTexConverter;
import dev.anvilcraft.lib.v2.rendering.optimization.occlusion.hiz.spd.SinglePassDownsampler;
import it.unimi.dsi.fastutil.ints.Int2ReferenceMap;
import it.unimi.dsi.fastutil.ints.Int2ReferenceOpenHashMap;
import it.unimi.dsi.fastutil.objects.Reference2IntLinkedOpenHashMap;
import it.unimi.dsi.fastutil.objects.Reference2IntMap;
import it.unimi.dsi.fastutil.objects.Reference2ObjectLinkedOpenHashMap;
import it.unimi.dsi.fastutil.objects.Reference2ObjectMap;
import it.unimi.dsi.fastutil.objects.ReferenceLinkedOpenHashSet;
import it.unimi.dsi.fastutil.objects.ReferenceSet;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import org.jetbrains.annotations.ApiStatus;
import org.joml.Vector2f;
import org.joml.Vector2i;
import org.joml.Vector4f;
import org.jspecify.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.IntConsumer;

@ApiStatus.Internal
public class HierarchicalZOcclusionCuller implements OcclusionCuller {
    private final Logger logger = LoggerFactory.getLogger("HierarchicalZOcclusionCuller");

    private final Minecraft minecraft;
    private final ALRGpuDeviceExtension gpuDeviceExtension;
    private final GpuDevice gpuDevice;

    private final SinglePassDownsampler downsampler;
    private final DepthTexConverter depthTexConverter;

    private final OcclusionTestCB testCB = new OcclusionTestCB();
    private final OcclusionTestSSBO inputSSBO = new OcclusionTestSSBO();

    private Slot[] slots = new Slot[0];
    private final SlotRing slotRing = new SlotRing();
    private long generation;
    private long frameId = -1;
    private boolean processed;
    private boolean closed;
    private boolean disabled;
    private long inputCapacity;
    private int elementCapacity;

    private final boolean bindlessTexturing;
    private int[] results;

    private FrameState previousFrameState = null;
    private FrameState currentFrameState = null;
    /// update interval for z-buffer mipmap
    ///
    /// interval = 0 -> update every frame
    ///
    @Setter
    @Getter
    private int mipmapUpdateInterval = 0;

    private int mipmapUpdateCd = mipmapUpdateInterval;
    private int culled = 0;
    private int size = 0;

    public HierarchicalZOcclusionCuller(ALRGpuDeviceExtension device) {
        this.minecraft = Minecraft.getInstance();
        this.gpuDeviceExtension = device;
        this.gpuDevice = (GpuDevice) device;

        RenderTarget mainRenderTarget = this.minecraft.getMainRenderTarget();

        this.downsampler = new SinglePassDownsampler(
            minecraft,
            gpuDeviceExtension,
            gpuDevice,
            mainRenderTarget
        );
        this.depthTexConverter = new DepthTexConverter(
            gpuDevice,
            gpuDeviceExtension,
            mainRenderTarget,
            this.downsampler.getPaddedWidth(),
            this.downsampler.getPaddedHeight()
        );
        this.bindlessTexturing = downsampler.isUseBindlessTexturing();
    }

    @Override
    public void onResize(int width, int height) {
        if (this.closed) return;
        // The window resize path provides no explicit GPU-idle guarantee here; discard old buffers without waiting.
        this.discardSlots();
        this.clearResults();
        this.processed = true;
        this.mipmapUpdateCd = 0;
        this.downsampler.onResize(width, height);
        this.depthTexConverter.onResize(
            width,
            height,
            this.downsampler.getPaddedWidth(),
            this.downsampler.getPaddedHeight()
        );
    }

    @Override
    public void beforeExtract() {
    }

    @Override
    public void beginRenderingFrame() {
        this.clearResults();
        if (!this.closed && !this.disabled) {
            try {
                this.fetchResults();
            } catch (RuntimeException exception) {
                this.disable(exception);
            }
        }
        this.frameId++;
        this.processed = false;
        this.currentFrameState = FrameState.create();
    }

    private void clearResults() {
        this.results = null;
        this.previousFrameState = null;
        this.size = 0;
        this.culled = 0;
    }

    private void fetchResults() {
        for (int index : this.slotRing.pending()) {
            Slot slot = this.slots[index];
            this.awaitSlot(index);
            if (slot.valid && slot.frameId == this.frameId && slot.generation == this.generation) {
                int[] snapshot = new int[slot.elementCount];
                CommandEncoder commandEncoder = this.gpuDevice.createCommandEncoder();
                try (GpuBuffer.MappedView mappedView = commandEncoder.mapBuffer(slot.outputBuffer, true, false)) {
                    IntBuffer intBuffer = mappedView.data().asIntBuffer();
                    intBuffer.get(0, snapshot, 0, snapshot.length);
                }
                this.results = snapshot;
                this.previousFrameState = slot.frameState;
                this.size = snapshot.length;
                for (int result : snapshot) {
                    if (result == 0) this.culled++;
                }
            }
            this.retireSlot(index);
        }
    }

    private void awaitSlot(int index) {
        Slot slot = this.slots[index];
        if (slot.fence == null) throw new IllegalStateException("Missing Hi-Z submission fence");
        if (slot.fence.awaitCompletion(0)) return;
        while (!slot.fence.awaitCompletion(1_000_000L)) {
            if (Thread.currentThread().isInterrupted()) {
                throw new IllegalStateException("Interrupted waiting for Hi-Z submission");
            }
        }
    }

    private void retireSlot(int index) {
        Slot slot = this.slots[index];
        slot.fence.close();
        slot.fence = null;
        slot.frameState = null;
        slot.valid = false;
        this.slotRing.release(index);
    }

    private void disable(RuntimeException exception) {
        this.disabled = true;
        this.clearResults();
        this.logger.error("Disabling Hi-Z occlusion after submission failure", exception);
        this.discardSlots();
    }

    @Override
    public void submitFeatureKey(OcclusionKey key, List<Object> feature) {
        if (this.closed || this.disabled || this.processed || this.currentFrameState == null) return;
        this.currentFrameState.keys.add(key);
        for (Object o : feature) {
            this.currentFrameState.keyAssociations.put(o, key);
        }
    }

    @Override
    public void processFeatures(CameraRenderState camera) {
        if (this.closed || this.disabled || this.processed || this.currentFrameState == null) return;
        this.processed = true;
        try {
            RenderTarget mainRenderTarget = this.minecraft.getMainRenderTarget();
            CommandEncoder commandEncoder = this.gpuDevice.createCommandEncoder();
            if (this.mipmapUpdateCd <= 0) {
                GpuTexture texture = this.depthTexConverter.runConvert(
                    commandEncoder,
                    mainRenderTarget.getDepthTexture()
                );
                this.downsampler.spdDispatch(commandEncoder, texture);
                this.mipmapUpdateCd = mipmapUpdateInterval;
            } else {
                this.mipmapUpdateCd--;
            }
            if (this.currentFrameState.keyAssociations.isEmpty()) return;
            this.dispatch(commandEncoder, camera, this.depthTexConverter.getOutput());
        } catch (RuntimeException exception) {
            this.clearResults();
            this.mipmapUpdateCd = 0;
            this.logger.error("Hi-Z submission failed; drawing conservatively", exception);
        }
    }

    private void dispatch(CommandEncoder commandEncoder, CameraRenderState camera, GpuTexture mip0) {
        if (this.currentFrameState.size() <= 0) return;

        FrameState currentFrameState = this.currentFrameState;
        int elementCount = currentFrameState.keys.size();

        Aabb[] aabbs = this.inputSSBO.getAabbs(elementCount);
        int i = 0;
        for (OcclusionKey key : currentFrameState.keys) {
            int id = i++;
            aabbs[id].set(key.getBoundingBox());
            currentFrameState.keyToIdMap.put(key, id);
        }

        Vector2i[] mipLayers = this.inputSSBO.getMipLayers();
        mipLayers[0] = new Vector2i(downsampler.getPaddedWidth(), downsampler.getPaddedHeight());

        GpuTexture[] mipTextures = downsampler.getMipTextures();

        for (i = 1; i < mipLayers.length; ++i) {
            boolean inRange = (i - 1) < downsampler.getMipLayers().length;
            if (inRange) {
                mipLayers[i].set(downsampler.getMipLayers()[i - 1]);
            } else {
                mipLayers[i].set(0, 0);
            }
        }

        this.inputSSBO.setMipLayers(mipLayers);
        this.inputSSBO.setAabbs(aabbs);
        this.inputSSBO.setActualSize(elementCount);

        this.testCB.setElementCount(elementCount);
        this.testCB.setMipLevels(Math.min(1 + mipTextures.length, OcclusionTestSSBO.MIP_LAYER_COUNT));
        this.testCB.setViewportSize(new Vector2f(
            downsampler.getFramebufferWidth(),
            downsampler.getFramebufferHeight()
        ));
        this.testCB.setCameraPos(new Vector4f((float) camera.pos.x, (float) camera.pos.y, (float) camera.pos.z, 1));
        this.testCB.getProjMat().set(camera.projectionMatrix);
        this.testCB.getCameraMat().set(camera.viewRotationMatrix);

        this.ensureBuffers(commandEncoder, elementCount);
        int slotIndex = this.slotRing.acquire(index -> {
            this.awaitSlot(index);
            this.retireSlot(index);
        });
        Slot slot = this.slots[slotIndex];
        slot.frameId = this.frameId;
        slot.generation = this.generation;
        slot.elementCount = elementCount;
        slot.frameState = currentFrameState;

        try {
            long actualRequestedSize = this.inputSSBO.actualSize(elementCount);
            ByteBuffer buffer = slot.staging.getBuffer(this.gpuDevice, commandEncoder, actualRequestedSize);
            buffer.clear();
            this.inputSSBO.write(buffer);
            buffer.position(0);

            ALRCommandEncoderExtension extension = (ALRCommandEncoderExtension) commandEncoder;
            extension.alrMemoryBarrier(MemoryBarrierFlag.BUFFER_UPDATE_BARRIER);
            slot.staging.copyToBuffer(
                commandEncoder,
                0,
                actualRequestedSize,
                slot.inputBuffer.slice(0, actualRequestedSize)
            );
            this.testCB.upload(commandEncoder, slot.testCBBuffer.slice());

            List<GpuTexture> textures = new ArrayList<>(OcclusionTestSSBO.MIP_LAYER_COUNT);
            textures.add(mip0);
            textures.addAll(Arrays.asList(mipTextures));
            while (textures.size() < OcclusionTestSSBO.MIP_LAYER_COUNT) {
                textures.add(mip0);
            }

            try (ALRComputePass pass = extension.alrCreateComputePass()) {
                if (this.bindlessTexturing) {
                    pass.setPipeline(ALRComputePipelines.HIZ_OCCLUSION_TEST_BINDLESS);
                } else {
                    pass.setPipeline(ALRComputePipelines.HIZ_OCCLUSION_TEST);
                }
                pass.bindUniformBlock(0, slot.testCBBuffer.slice());
                pass.bindShaderStorage(0, slot.inputBuffer.slice());
                pass.bindShaderStorage(1, slot.outputBuffer.slice());
                if (this.bindlessTexturing) {
                    pass.bindBindlessImageArray("uInputs", textures);
                } else {
                    pass.bindArrayOfTexture(0, textures, true, false);
                }
                pass.dispatchWorkgroups(Math.ceilDiv(elementCount, 32), 1, 1);
                pass.memoryBarrier(
                    MemoryBarrierFlag.SHADER_IMAGE_ACCESS_BARRIER,
                    MemoryBarrierFlag.SHADER_STORAGE_BARRIER
                );
            }
            if (StagingSupport.usesPersistentMapping(this.gpuDevice)) {
                extension.alrMemoryBarrier(
                    MemoryBarrierFlag.CLIENT_MAPPED_BUFFER_BARRIER,
                    MemoryBarrierFlag.BUFFER_UPDATE_BARRIER
                );
            } else {
                extension.alrMemoryBarrier(MemoryBarrierFlag.BUFFER_UPDATE_BARRIER);
            }
            slot.fence = commandEncoder.createFence();
            slot.valid = true;
            this.slotRing.submit(slotIndex);
        } catch (RuntimeException exception) {
            slot.valid = false;
            try {
                if (slot.fence == null) slot.fence = commandEncoder.createFence();
                this.slotRing.submit(slotIndex);
            } catch (RuntimeException fenceException) {
                exception.addSuppressed(fenceException);
                this.disable(exception);
            }
            throw exception;
        }
    }

    private void ensureBuffers(CommandEncoder commandEncoder, int elementCount) {
        long requiredInputSize = inputSSBO.getDefinition().size(BufferLayout.STD430);
        if (this.slots.length == SlotRing.COUNT && this.inputCapacity >= requiredInputSize && this.elementCapacity >= elementCount)
            return;

        long newInputCapacity = Math.max(this.inputCapacity, requiredInputSize);
        int newElementCapacity = Math.max(this.elementCapacity, elementCount);
        Slot[] replacement = new Slot[SlotRing.COUNT];
        try {
            for (int i = 0; i < replacement.length; i++) {
                Slot slot = new Slot();
                replacement[i] = slot;
                slot.staging = StagingSupport.createInstance(this.gpuDevice, "Hi-Z Occlusion Test Staging #" + i);
                slot.staging.ensureCapacity(this.gpuDevice, commandEncoder, newInputCapacity);
                slot.inputBuffer = gpuDevice.createBuffer(
                    () -> "Hi-Z Occlusion Test Input",
                    GpuBuffer.USAGE_COPY_DST | GpuBufferConstants.USAGE_SHADER_STORAGE,
                    newInputCapacity
                );
                slot.outputBuffer = gpuDevice.createBuffer(
                    () -> "Hi-Z Occlusion Test Output",
                    GpuBuffer.USAGE_COPY_DST | GpuBufferConstants.USAGE_SHADER_STORAGE | GpuBuffer.USAGE_MAP_READ,
                    (long) newElementCapacity * Integer.BYTES
                );
                slot.testCBBuffer = gpuDevice.createBuffer(
                    () -> "Hi-Z Occlusion Test CB",
                    GpuBuffer.USAGE_COPY_DST | GpuBuffer.USAGE_UNIFORM,
                    OcclusionTestCB.SIZE
                );
            }
        } catch (RuntimeException exception) {
            this.closeSlots(replacement);
            throw exception;
        }
        this.discardSlots();
        this.slots = replacement;
        this.inputCapacity = newInputCapacity;
        this.elementCapacity = newElementCapacity;
    }

    private void discardSlots() {
        Slot[] discarded = this.slots;
        this.slots = new Slot[0];
        this.slotRing.clear();
        this.inputCapacity = 0;
        this.elementCapacity = 0;
        this.generation++;
        this.closeSlots(discarded);
    }

    private void closeSlots(Slot[] slots) {
        RuntimeException failure = null;
        for (Slot slot : slots) {
            if (slot == null) continue;
            try {
                slot.close();
            } catch (RuntimeException exception) {
                if (failure == null) {
                    failure = exception;
                } else {
                    failure.addSuppressed(exception);
                }
            }
        }
        if (failure != null) {
            this.logger.error("Failed to close Hi-Z slots", failure);
        }
    }

    @Override
    public boolean isEmpty() {
        return this.previousFrameState == null || this.previousFrameState.keyAssociations.isEmpty();
    }

    @Override
    public @Nullable CullingStatistics collectStatistics() {
        return new CullingStatistics(
            this.size,
            -1,
            -1,
            this.culled,
            this.size - this.culled,
            this.previousFrameState == null ? 0 : this.previousFrameState.keyAssociations.size(),
            null
        );
    }

    @Override
    public boolean shouldDraw(Object feature) {
        if (this.currentFrameState == null || this.previousFrameState == null || this.results == null) return true;
        OcclusionKey key = this.currentFrameState.keyAssociations.get(feature);
        if (key == null) return true;
        int id = this.previousFrameState.keyToIdMap.getOrDefault(key, -1);
        if (id < 0 || id >= this.size) return true;
        return this.results[id] > 0;
    }

    @Override
    public void close() {
        if (this.closed) return;
        this.closed = true;
        this.discardSlots();
        this.clearResults();
        this.currentFrameState = null;
        try {
            this.depthTexConverter.close();
        } finally {
            this.downsampler.close();
        }
    }

    private static class Slot implements AutoCloseable {
        private StagingSupport staging;
        private GpuBuffer inputBuffer;
        private GpuBuffer outputBuffer;
        private GpuBuffer testCBBuffer;
        private GpuFence fence;
        private long frameId;
        private long generation;
        private int elementCount;
        private FrameState frameState;
        private boolean valid;

        @Override
        public void close() {
            this.frameState = null;
            this.valid = false;
            try {
                if (this.fence != null) this.fence.close();
            } finally {
                this.fence = null;
                try {
                    if (this.staging != null) this.staging.close();
                } finally {
                    this.staging = null;
                    try {
                        if (this.inputBuffer != null) this.inputBuffer.close();
                    } finally {
                        this.inputBuffer = null;
                        try {
                            if (this.outputBuffer != null) this.outputBuffer.close();
                        } finally {
                            this.outputBuffer = null;
                            try {
                                if (this.testCBBuffer != null) this.testCBBuffer.close();
                            } finally {
                                this.testCBBuffer = null;
                            }
                        }
                    }
                }
            }
        }
    }

    static class SlotRing {
        static final int COUNT = 3;
        private final boolean[] occupied = new boolean[COUNT];
        private final ArrayDeque<Integer> pending = new ArrayDeque<>();
        private int next;

        int acquire(IntConsumer completeOldest) {
            for (int i = 0; i < COUNT; i++) {
                int index = (this.next + i) % COUNT;
                if (this.occupied[index]) continue;
                this.occupied[index] = true;
                this.next = (index + 1) % COUNT;
                return index;
            }
            if (this.pending.isEmpty()) {
                throw new IllegalStateException("Hi-Z slots reserved without submission");
            }
            int oldest = this.pending.getFirst();
            completeOldest.accept(oldest);
            if (this.occupied[oldest]) {
                throw new IllegalStateException("Hi-Z slot completion did not release ownership");
            }
            return this.acquire(completeOldest);
        }

        void submit(int index) {
            if (!this.occupied[index] || this.pending.contains(index)) {
                throw new IllegalStateException("Invalid Hi-Z slot submission");
            }
            this.pending.addLast(index);
        }

        void release(int index) {
            if (!this.occupied[index]) {
                throw new IllegalStateException("Hi-Z slot is not owned");
            }
            this.pending.remove(index);
            this.occupied[index] = false;
        }

        List<Integer> pending() {
            return List.copyOf(this.pending);
        }

        void clear() {
            this.pending.clear();
            Arrays.fill(this.occupied, false);
            this.next = 0;
        }
    }

    record FrameState(
        ReferenceSet<OcclusionKey> keys,
        Reference2ObjectMap<Object, OcclusionKey> keyAssociations,
        Int2ReferenceMap<OcclusionKey> idToKeyMap,
        Reference2IntMap<OcclusionKey> keyToIdMap
    ) {
        public static FrameState create() {
            return new FrameState(
                new ReferenceLinkedOpenHashSet<>(),
                new Reference2ObjectLinkedOpenHashMap<>(),
                new Int2ReferenceOpenHashMap<>(),
                new Reference2IntLinkedOpenHashMap<>()
            );
        }

        public int size() {
            return this.keys.size();
        }
    }

}
