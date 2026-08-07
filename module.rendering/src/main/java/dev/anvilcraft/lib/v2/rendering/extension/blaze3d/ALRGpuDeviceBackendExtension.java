package dev.anvilcraft.lib.v2.rendering.extension.blaze3d;

import org.jetbrains.annotations.ApiStatus;

import dev.anvilcraft.lib.v2.rendering.extension.blaze3d.compute.shader.ALRComputeProgramInstance;
import dev.anvilcraft.lib.v2.rendering.extension.blaze3d.compute.shader.ALRComputeProgramInstanceKey;
import dev.anvilcraft.lib.v2.rendering.extension.blaze3d.query.GpuQueryObject;

import java.util.function.Supplier;

@ApiStatus.Internal
public interface ALRGpuDeviceBackendExtension {
    ALRComputeProgramInstance alrCompileComputeShader(ALRComputeProgramInstanceKey instanceKey);

    void alrDestroyComputeShader(ALRComputeProgramInstance instance);

    void alrPushDebugGroup(Supplier<String> name);

    void alrPopDebugGroup();

    GpuQueryObject alrCreateSamplesQuery();
}
