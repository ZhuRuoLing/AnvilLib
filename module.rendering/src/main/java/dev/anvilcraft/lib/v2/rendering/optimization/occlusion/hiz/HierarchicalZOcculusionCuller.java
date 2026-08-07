package dev.anvilcraft.lib.v2.rendering.optimization.occlusion.hiz;

import dev.anvilcraft.lib.v2.rendering.optimization.occlusion.OcclusionCuller;
import dev.anvilcraft.lib.v2.rendering.optimization.occlusion.OcclusionKey;
import net.minecraft.client.Camera;
import net.minecraft.client.renderer.state.level.CameraRenderState;

public class HierarchicalZOcculusionCuller implements OcclusionCuller {
    @Override
    public void beginFrame() {

    }

    @Override
    public void submitFeatureKey(OcclusionKey key, Object feature) {

    }

    @Override
    public void processFeatures(CameraRenderState camera) {

    }

    @Override
    public boolean shouldDraw(OcclusionKey key, Object feature) {
        return false;
    }
}
