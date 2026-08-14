package dev.anvilcraft.lib.v2.rendering.extension.blaze3d.compute.pipeline.bindings;

import dev.anvilcraft.lib.v2.rendering.extension.blaze3d.compute.pipeline.ALRComputePass;
import dev.anvilcraft.lib.v2.rendering.extension.blaze3d.compute.shader.ShaderResourceType;

public interface ComputeBindingLayout<T> {
    ShaderResourceType type();

    String name();

    void apply(int bindingPoint, T resource, ALRComputePass computePass);

    /// @return binding point incremental
    default int applyOrdered(int bindingPointStart, T resource, ALRComputePass computePass) {
        this.apply(bindingPointStart, resource, computePass);
        return 1;
    }
}
