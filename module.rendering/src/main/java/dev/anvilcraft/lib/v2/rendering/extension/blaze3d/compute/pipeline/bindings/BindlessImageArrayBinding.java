package dev.anvilcraft.lib.v2.rendering.extension.blaze3d.compute.pipeline.bindings;

import com.google.common.base.Preconditions;
import com.mojang.blaze3d.textures.GpuTexture;
import dev.anvilcraft.lib.v2.rendering.extension.blaze3d.compute.pipeline.ALRComputePass;
import dev.anvilcraft.lib.v2.rendering.extension.blaze3d.compute.shader.ShaderResourceType;

import java.util.List;

public record BindlessImageArrayBinding(
    String name,
    boolean read,
    boolean write,
    int size
) implements ComputeBindingLayout<List<GpuTexture>> {

    public BindlessImageArrayBinding {
        if (!read && !write) {
            throw new IllegalArgumentException("BindlessImageArrayBinding does not allow both read and write are false");
        }
    }

    @Override
    public ShaderResourceType type() {
        return ShaderResourceType.TEXTURE_OR_IMAGE;
    }

    /// Just assume iterating over the list passed in is ordered.
    @Override
    public int applyOrdered(int bindingPointStart, List<GpuTexture> resource, ALRComputePass computePass) {
        Preconditions.checkArgument(
            this.size >= resource.size(),
            "resource must not have elements more than this.size"
        );
        computePass.bindBindlessImageArray(name, resource, read, write);
        return 0;
    }
}
