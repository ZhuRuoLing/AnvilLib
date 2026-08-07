package dev.anvilcraft.lib.v2.rendering.extension.blaze3d.compute.shader;

import dev.anvilcraft.lib.v2.rendering.AnvilLibRendering;
import net.minecraft.client.renderer.ShaderDefines;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Internal
public record ALRComputeProgramInstance(int id, ALRComputeProgramInstanceKey key) {
    public static final ALRComputeProgramInstance INVALID = new ALRComputeProgramInstance(
        0,
        new ALRComputeProgramInstanceKey(
            AnvilLibRendering.location("compute/invalid"),
            ShaderDefines.builder().build()
        )
    );

}
