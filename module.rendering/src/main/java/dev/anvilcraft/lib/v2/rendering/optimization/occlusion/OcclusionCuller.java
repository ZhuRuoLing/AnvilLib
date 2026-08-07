package dev.anvilcraft.lib.v2.rendering.optimization.occlusion;

import com.mojang.blaze3d.systems.GpuDevice;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import org.jspecify.annotations.Nullable;

public interface OcclusionCuller {

    void beginFrame();

    void submitFeatureKey(OcclusionKey key, Object feature);

    void processFeatures(CameraRenderState camera);

    boolean shouldDraw(OcclusionKey key, Object feature);

    @SuppressWarnings("ConstantValue")
    @Nullable
    static OcclusionCuller createInstance(GpuDevice device) {
        OcclusionCuller instance;
        if (OcclusionMethod.HIERARCHICAL_Z.isSupported()
            && (instance = OcclusionMethod.HIERARCHICAL_Z.createInstance(device)) != null
        ) {
            return instance;
        }

        if (OcclusionMethod.GPU_QUERY.isSupported()
            && (instance = OcclusionMethod.GPU_QUERY.createInstance(device)) != null
        ) {
            return instance;
        }

        return null;
    }
}
