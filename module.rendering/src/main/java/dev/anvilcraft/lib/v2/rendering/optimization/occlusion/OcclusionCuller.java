package dev.anvilcraft.lib.v2.rendering.optimization.occlusion;

import com.mojang.blaze3d.systems.GpuDevice;
import dev.anvilcraft.lib.v2.rendering.ALROptions;
import net.minecraft.client.renderer.SubmitNodeStorage;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import org.jspecify.annotations.Nullable;

import java.util.List;

public interface OcclusionCuller {

    void onResize(int newWidth, int newHeight);

    /// called on a frame before all features are extracted
    void beginFrame();

    /// OcclusionCuller relies on OcclusionKey to distinguish features between frames,
    /// so the key param is also required to be the same object between frames.
    void submitFeatureKey(OcclusionKey key, List<Object> feature);

    void processFeatures(CameraRenderState camera);

    boolean shouldDraw(Object feature);

    default OcclusionSubmitNodeStorage wrapSubmitNodeStorage(SubmitNodeStorage original) {
        return new OcclusionSubmitNodeStorage(this, original);
    }

    @SuppressWarnings("ConstantValue")
    @Nullable
    static OcclusionCuller createInstance(GpuDevice device) {
        OcclusionCuller instance;

        String forcedImpl = ALROptions.OCCLUSION_CULLING_FORCE_IMPL;
        if (forcedImpl != null) {
            try {
                instance = OcclusionMethod.valueOf(forcedImpl).createInstance(device);
            } catch (IllegalArgumentException _) {
                instance = null;
            }
            if (instance != null) {
                return instance;
            }
        }
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
