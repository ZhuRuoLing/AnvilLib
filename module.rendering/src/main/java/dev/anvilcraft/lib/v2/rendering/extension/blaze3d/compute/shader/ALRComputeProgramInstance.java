package dev.anvilcraft.lib.v2.rendering.extension.blaze3d.compute.shader;

import dev.anvilcraft.lib.v2.rendering.AnvilLibRendering;
import dev.anvilcraft.lib.v2.rendering.extension.blaze3d.ALRGpuDeviceBackendExtension;
import dev.anvilcraft.lib.v2.rendering.extension.blaze3d.NamedUniformAccess;
import dev.anvilcraft.lib.v2.rendering.extension.blaze3d.compute.pipeline.ALRComputePipeline;
import net.minecraft.client.renderer.ShaderDefines;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Internal
public record ALRComputeProgramInstance(
    int id,
    ALRComputeProgramInstanceKey key,
    ALRComputePipeline owner
) implements NamedUniformAccess {
    public static final ALRComputeProgramInstance INVALID = new ALRComputeProgramInstance(
        0,
        new ALRComputeProgramInstanceKey(
            AnvilLibRendering.location("compute/invalid"),
            ShaderDefines.builder().build()
        ),
        null
    );

    @Override
    public int getUniformLocation(String name, ALRGpuDeviceBackendExtension device) {
        return device.alrGetUniformLocation(this, owner, name);
    }
}
