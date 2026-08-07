package dev.anvilcraft.lib.v2.rendering.optimization.occlusion.query;

import com.mojang.blaze3d.systems.CommandEncoder;
import com.mojang.blaze3d.systems.GpuDevice;
import dev.anvilcraft.lib.v2.rendering.extension.blaze3d.ALRGpuDeviceExtension;
import dev.anvilcraft.lib.v2.rendering.extension.blaze3d.query.GpuQueryObject;
import dev.anvilcraft.lib.v2.rendering.optimization.occlusion.OcclusionCuller;
import dev.anvilcraft.lib.v2.rendering.optimization.occlusion.OcclusionKey;
import lombok.Getter;
import net.minecraft.client.Camera;
import net.minecraft.client.renderer.state.level.CameraRenderState;

public class GpuQueryOcclusionCuller implements OcclusionCuller {

    @Getter
    private final CommandEncoder commandEncoder;
    private final GpuSampleQueryRingBuffer sampleQueryPool;
    private final QueryBufferPackRingBuffer bufferPackPool;
    private final ALRGpuDeviceExtension extension;

    private FrameState previousFrameState = null;
    private FrameState currentFrameState = null;

    public GpuQueryOcclusionCuller(ALRGpuDeviceExtension extension) {
        this.extension = extension;
        this.sampleQueryPool = new GpuSampleQueryRingBuffer(extension);
        GpuDevice gpuDevice = (GpuDevice) extension;
        this.commandEncoder = gpuDevice.createCommandEncoder();
        this.bufferPackPool = new QueryBufferPackRingBuffer(
            new QueryBufferPack.CreationContext(
                commandEncoder,
                gpuDevice
            )
        );
    }

    @Override
    public void beginFrame() {
        if (this.currentFrameState == null) {
            this.currentFrameState = new FrameState(this);
            return;
        }
        if (this.previousFrameState != null) {
            this.previousFrameState.close();
        }
        this.previousFrameState = this.currentFrameState;
        this.previousFrameState.fetchResults();
        this.currentFrameState = new FrameState(this);
    }

    @Override
    public void submitFeatureKey(OcclusionKey key, Object feature) {
        this.currentFrameState.addKey(key);
    }

    @Override
    public void processFeatures(CameraRenderState camera) {
        this.currentFrameState.runQueries(camera);
    }

    @Override
    public boolean shouldDraw(OcclusionKey key, Object feature) {
        if (previousFrameState == null) {
            return false;
        }
        return this.previousFrameState.shouldDraw(key);
    }

    public GpuQueryObject acquireQuery() {
        return this.sampleQueryPool.acquire();
    }

    public void releaseQuery(GpuQueryObject query) {
        this.sampleQueryPool.release(query);
    }

    public QueryBufferPack acquireBuffer() {
        return this.bufferPackPool.acquire();
    }

    public void releaseBuffer(QueryBufferPack bufferPack) {
        this.bufferPackPool.release(bufferPack);
    }
}
