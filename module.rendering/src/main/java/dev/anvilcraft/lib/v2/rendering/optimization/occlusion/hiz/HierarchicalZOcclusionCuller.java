package dev.anvilcraft.lib.v2.rendering.optimization.occlusion.hiz;

import com.mojang.blaze3d.pipeline.RenderTarget;
import com.mojang.blaze3d.systems.CommandEncoder;
import com.mojang.blaze3d.systems.GpuDevice;
import com.mojang.blaze3d.textures.GpuTexture;
import dev.anvilcraft.lib.v2.rendering.extension.blaze3d.ALRGpuDeviceExtension;
import dev.anvilcraft.lib.v2.rendering.optimization.occlusion.CullingStatistics;
import dev.anvilcraft.lib.v2.rendering.optimization.occlusion.OcclusionCuller;
import dev.anvilcraft.lib.v2.rendering.optimization.occlusion.OcclusionKey;
import dev.anvilcraft.lib.v2.rendering.optimization.occlusion.hiz.converter.DepthTexConverter;
import dev.anvilcraft.lib.v2.rendering.optimization.occlusion.hiz.spd.SinglePassDownsampler;
import it.unimi.dsi.fastutil.objects.Reference2ObjectLinkedOpenHashMap;
import it.unimi.dsi.fastutil.objects.Reference2ObjectMap;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import org.jspecify.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class HierarchicalZOcclusionCuller implements OcclusionCuller {


    private final Minecraft minecraft;
    private final ALRGpuDeviceExtension gpuDeviceExtension;
    private final GpuDevice gpuDevice;

    private final SinglePassDownsampler downsampler;
    private final DepthTexConverter depthTexConverter;

    private final Reference2ObjectMap<Object, OcclusionKey> keyAssociations = new Reference2ObjectLinkedOpenHashMap<>();

    private final List<OcclusionKey> occlusionKeys = new ArrayList<>();

    /// update interval for z-buffer mipmap
    ///
    /// interval = 0 -> update every frame
    ///
    @Setter
    @Getter
    private int mipmapUpdateInterval = 0;

    private int mipmapUpdateCd = mipmapUpdateInterval;

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
    }

    @Override
    public void onResize(int width, int height) {
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
        RenderTarget mainRenderTarget = this.minecraft.getMainRenderTarget();
        CommandEncoder commandEncoder = this.gpuDevice.createCommandEncoder();
        GpuTexture texture = this.depthTexConverter.runConvert(
            commandEncoder,
            mainRenderTarget.getDepthTexture()
        );
        this.downsampler.spdDispatch(commandEncoder, texture);
    }

    @Override
    public boolean isEmpty() {
        return this.keyAssociations.isEmpty();
    }

    @Override
    public @Nullable CullingStatistics collectStatistics() {
        return null;
    }

    @Override
    public boolean shouldDraw(Object feature) {
        return true;
    }

    @Override
    public void close() throws Exception {
    }
}
