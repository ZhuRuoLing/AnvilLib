package dev.anvilcraft.lib.v2.rendering.optimization.occlusion.hiz;

import com.mojang.blaze3d.buffers.GpuBuffer;
import com.mojang.blaze3d.pipeline.RenderTarget;
import com.mojang.blaze3d.systems.CommandEncoder;
import com.mojang.blaze3d.systems.GpuDevice;
import com.mojang.blaze3d.textures.GpuTexture;
import dev.anvilcraft.lib.v2.rendering.extension.blaze3d.ALRGpuDeviceExtension;
import dev.anvilcraft.lib.v2.rendering.extension.blaze3d.ExtendedTextureFormat;
import dev.anvilcraft.lib.v2.rendering.foundation.buffers.GpuBufferConstants;
import dev.anvilcraft.lib.v2.rendering.optimization.occlusion.OcclusionCuller;
import dev.anvilcraft.lib.v2.rendering.optimization.occlusion.OcclusionKey;
import dev.anvilcraft.lib.v2.rendering.util.MemoryAccess;
import it.unimi.dsi.fastutil.objects.Reference2ObjectLinkedOpenHashMap;
import it.unimi.dsi.fastutil.objects.Reference2ObjectMap;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.util.Mth;
import org.joml.Vector2f;
import org.lwjgl.system.MemoryStack;

import java.nio.ByteBuffer;
import java.util.List;

public class HierarchicalZOcclusionCuller implements OcclusionCuller {
    /// uint * 6
    public static final int SPD_GLOBAL_ATOMIC_COUNTER_SIZE = 4 * 6;

    private final Minecraft minecraft;
    private final ALRGpuDeviceExtension gpuDeviceExtension;
    private final GpuDevice gpuDevice;

    private final Reference2ObjectMap<Object, OcclusionKey> keyAssociations = new Reference2ObjectLinkedOpenHashMap<>();

    private final SPDConstantBuffer spdParams = new SPDConstantBuffer();
    private final ConvertDepthParamsUbo convertParams = new ConvertDepthParamsUbo();

    private final GpuBuffer spdParamsBuffer;
    private final GpuBuffer convertParamsBuffer;
    private final GpuBuffer spdGlobalAtomicCounterBuffer;

    private int framebufferWidth;
    private int framebufferHeight;
    private int paddedWidth;
    private int paddedHeight;

    private int dispatchDimensionX;
    private int dispatchDimensionY;

    /// mip layer count, excluding input layer (mip 0)
    private int mipLayerCount = 0;
    private GpuTexture[] mipTextures;
    private MipLayer[] mipLayers;

    /// update interval for z-buffer mipmap
    ///
    /// interval = 0 -> update every frame
    ///
    private int mipmapUpdateInterval = 0;

    public HierarchicalZOcclusionCuller(ALRGpuDeviceExtension device) {
        this.minecraft = Minecraft.getInstance();
        this.gpuDeviceExtension = device;
        this.gpuDevice = (GpuDevice) device;

        RenderTarget mainRenderTarget = this.minecraft.getMainRenderTarget();

        this.spdParamsBuffer = gpuDevice.createBuffer(
            () -> "SPD Constant Buffer",
            GpuBuffer.USAGE_COPY_DST | GpuBuffer.USAGE_UNIFORM,
            SPDConstantBuffer.SIZE
        );

        this.convertParamsBuffer = gpuDevice.createBuffer(
            () -> "SPD Depth Convert Params",
            GpuBuffer.USAGE_COPY_DST | GpuBuffer.USAGE_UNIFORM,
            ConvertDepthParamsUbo.SIZE
        );

        this.spdGlobalAtomicCounterBuffer = gpuDevice.createBuffer(
            () -> "SPD Global Atomic Counter",
            GpuBuffer.USAGE_COPY_DST | GpuBuffer.USAGE_MAP_READ | GpuBuffer.USAGE_MAP_WRITE | GpuBufferConstants.USAGE_SHADER_STORAGE,
            SPD_GLOBAL_ATOMIC_COUNTER_SIZE
        );

        this.onResize(mainRenderTarget.width, mainRenderTarget.height);

        this.clearAtomicCounter();
    }

    @Override
    public void onResize(int width, int height) {
        this.framebufferWidth = width;
        this.framebufferHeight = height;

        this.paddedWidth = Math.ceilDiv(width, 64) * 64;
        this.paddedHeight = Math.ceilDiv(height, 64) * 64;

        this.mipLayerCount = Math.min(
            Mth.floor(
                Mth.log2(
                    Math.max(
                        this.paddedWidth,
                        this.paddedHeight
                    )
                )
            ),
            12
        );

        this.dispatchDimensionX = Mth.ceil(paddedWidth / 64f);
        this.dispatchDimensionY = Mth.ceil(paddedHeight / 64f);

        this.deleteTextures();

        int slotCount = mipLayerCount + 1;
        this.mipTextures = new GpuTexture[slotCount];
        this.mipLayers = new MipLayer[slotCount];

        for (int i = 0; i < slotCount; i++) {
            int mipW = Math.max(1, this.paddedWidth >> i);
            int mipH = Math.max(1, this.paddedHeight >> i);

            MipLayer mipLayer = new MipLayer();
            mipLayer.setWidth(mipW);
            mipLayer.setHeight(mipH);

            GpuTexture texture = this.gpuDeviceExtension.alrCreateExtendedTexture(
                "HierarchicalZ Mip Chain Image #" + i,
                GpuTexture.USAGE_COPY_SRC | GpuTexture.USAGE_COPY_DST | GpuTexture.USAGE_TEXTURE_BINDING,
                ExtendedTextureFormat.R32F,
                mipW,
                mipH,
                1,
                1
            );

            this.mipTextures[i] = texture;
            this.mipLayers[i] = mipLayer;
        }

        CommandEncoder commandEncoder = gpuDevice.createCommandEncoder();
        this.ffxSpdSetup(commandEncoder);
        this.depthConvertSetup(commandEncoder);
    }

    /// Setup required constant values for SPD (CPU).
    private void ffxSpdSetup(CommandEncoder commandEncoder) {
        this.spdParams.setMips(this.mipLayerCount);
        this.spdParams.setNumWorkGroups(this.dispatchDimensionX * this.dispatchDimensionY);
        this.spdParams.setWorkGroupOffset(new Vector2f(0, 0));
        this.spdParams.setInvInputSize(new Vector2f(1.0f / this.paddedWidth, 1.0f / this.paddedHeight));

        this.spdParams.upload(commandEncoder, this.spdParamsBuffer.slice());
    }

    private void depthConvertSetup(CommandEncoder commandEncoder) {
        this.convertParams.setWidth(this.framebufferWidth);
        this.convertParams.setHeight(this.framebufferHeight);
        this.convertParams.setPadValue(1);

        this.convertParams.upload(commandEncoder, this.convertParamsBuffer.slice());
    }

    private void clearAtomicCounter() {
        CommandEncoder commandEncoder = gpuDevice.createCommandEncoder();

        try (MemoryStack memoryStack = MemoryStack.stackPush()) {
            ByteBuffer buffer = memoryStack.malloc(SPD_GLOBAL_ATOMIC_COUNTER_SIZE);
            MemoryAccess.memset(MemoryAccess.memAddress(buffer), SPD_GLOBAL_ATOMIC_COUNTER_SIZE, (byte) 0);
            commandEncoder.writeToBuffer(spdGlobalAtomicCounterBuffer.slice(), buffer);
        }
    }

    @Override
    public void beginFrame() {
        this.keyAssociations.clear();
    }

    @Override
    public void submitFeatureKey(OcclusionKey key, List<Object> feature) {
        for (Object o : feature) {
            this.keyAssociations.put(o, key);
        }
    }

    @Override
    public void processFeatures(CameraRenderState camera) {

    }

    @Override
    public boolean shouldDraw(Object feature) {
        return true;
    }

    private void deleteTextures() {
        if (mipTextures != null) {
            for (GpuTexture mipTexture : mipTextures) {
                mipTexture.close();
            }
        }
    }
}
