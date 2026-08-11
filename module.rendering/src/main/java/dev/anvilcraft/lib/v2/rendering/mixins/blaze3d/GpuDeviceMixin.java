package dev.anvilcraft.lib.v2.rendering.mixins.blaze3d;

import com.mojang.blaze3d.systems.GpuDevice;
import com.mojang.blaze3d.systems.GpuDeviceBackend;
import dev.anvilcraft.lib.v2.rendering.extension.blaze3d.ALRGpuDeviceBackendExtension;
import dev.anvilcraft.lib.v2.rendering.extension.blaze3d.ALRGpuDeviceExtension;
import dev.anvilcraft.lib.v2.rendering.extension.blaze3d.ALRHICapabilities;
import dev.anvilcraft.lib.v2.rendering.extension.blaze3d.compute.shader.ALRComputeProgramInstance;
import dev.anvilcraft.lib.v2.rendering.extension.blaze3d.compute.shader.ALRComputeProgramInstanceKey;
import dev.anvilcraft.lib.v2.rendering.extension.blaze3d.query.GpuQueryObject;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

import java.util.function.Supplier;

@SuppressWarnings("AddedMixinMembersNamePattern")
@Mixin(GpuDevice.class)
public class GpuDeviceMixin implements ALRGpuDeviceExtension {
    @Shadow
    @Final
    private GpuDeviceBackend backend;

    @Override
    public ALRComputeProgramInstance alrCompileComputeShader(ALRComputeProgramInstanceKey instanceKey) {
        return alrBackend().alrCompileComputeShader(instanceKey);
    }

    @Override
    public void alrDestroyComputeShader(ALRComputeProgramInstance instance) {
        alrBackend().alrDestroyComputeShader(instance);
    }

    @Override
    public GpuQueryObject alrCreateSamplesQuery() {
        return alrBackend().alrCreateSamplesQuery();
    }

    @Override
    public void alrPushDebugGroup(Supplier<String> message) {
        alrBackend().alrPushDebugGroup(message);
    }

    @Override
    public void alrPopDebugGroup() {
        alrBackend().alrPopDebugGroup();
    }

    @Override
    public ALRHICapabilities alrhiCreateCapabilities() {
        return alrBackend().alrhiCreateCapabilities();
    }

    @Unique
    private ALRGpuDeviceBackendExtension alrBackend() {
        return ((ALRGpuDeviceBackendExtension) this.backend);
    }
}
