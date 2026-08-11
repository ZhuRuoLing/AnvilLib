package dev.anvilcraft.lib.v2.rendering.extension.blaze3d;

import dev.anvilcraft.lib.v2.rendering.extension.blaze3d.compute.shader.ALRComputeProgramInstance;
import dev.anvilcraft.lib.v2.rendering.extension.blaze3d.compute.shader.ALRComputeProgramInstanceKey;
import dev.anvilcraft.lib.v2.rendering.extension.blaze3d.query.GpuQueryObject;

import java.util.function.Supplier;

public interface ALRGpuDeviceExtension {
    ALRComputeProgramInstance alrCompileComputeShader(ALRComputeProgramInstanceKey instanceKey);

    void alrDestroyComputeShader(ALRComputeProgramInstance instance);

    GpuQueryObject alrCreateSamplesQuery();

    void alrPushDebugGroup(Supplier<String> message);

    void alrPopDebugGroup();

    ALRHICapabilities alrhiCreateCapabilities();
}
