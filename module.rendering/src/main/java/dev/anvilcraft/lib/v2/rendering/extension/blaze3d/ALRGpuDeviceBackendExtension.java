package dev.anvilcraft.lib.v2.rendering.extension.blaze3d;

import com.mojang.blaze3d.textures.GpuTexture;
import org.jetbrains.annotations.ApiStatus;

import dev.anvilcraft.lib.v2.rendering.extension.blaze3d.compute.shader.ALRComputeProgramInstance;
import dev.anvilcraft.lib.v2.rendering.extension.blaze3d.compute.shader.ALRComputeProgramInstanceKey;
import dev.anvilcraft.lib.v2.rendering.extension.blaze3d.query.GpuQueryObject;
import org.jspecify.annotations.Nullable;

import java.util.function.Supplier;

@ApiStatus.Internal
public interface ALRGpuDeviceBackendExtension {
    ALRComputeProgramInstance alrCompileComputeShader(ALRComputeProgramInstanceKey instanceKey);

    void alrDestroyComputeShader(ALRComputeProgramInstance instance);

    void alrPushDebugGroup(Supplier<String> name);

    void alrPopDebugGroup();

    GpuQueryObject alrCreateSamplesQuery();

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
