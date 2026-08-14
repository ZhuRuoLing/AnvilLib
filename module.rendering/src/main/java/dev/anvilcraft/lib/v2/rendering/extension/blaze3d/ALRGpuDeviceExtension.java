package dev.anvilcraft.lib.v2.rendering.extension.blaze3d;

import com.mojang.blaze3d.textures.GpuTexture;
import com.mojang.blaze3d.textures.TextureFormat;
import dev.anvilcraft.lib.v2.rendering.extension.blaze3d.compute.shader.ALRComputeProgramInstance;
import dev.anvilcraft.lib.v2.rendering.extension.blaze3d.compute.shader.ALRComputeProgramInstanceKey;
import dev.anvilcraft.lib.v2.rendering.extension.blaze3d.query.GpuQueryObject;
import org.jspecify.annotations.Nullable;

import java.util.function.Supplier;

public interface ALRGpuDeviceExtension {
    ALRComputeProgramInstance alrCompileComputeShader(ALRComputeProgramInstanceKey instanceKey);

    void alrDestroyComputeShader(ALRComputeProgramInstance instance);

    GpuQueryObject alrCreateSamplesQuery();

    void alrPushDebugGroup(Supplier<String> message);

    void alrPopDebugGroup();

    ALRHICapabilities alrhiCreateCapabilities();

    GpuTexture alrCreateExtendedTexture(
        @Nullable String label,
        @GpuTexture.Usage int usage,
        ExtendedTextureFormat format,
        int width,
        int height,
        int depthOrLayers,
        int mipLevels
    );
}
