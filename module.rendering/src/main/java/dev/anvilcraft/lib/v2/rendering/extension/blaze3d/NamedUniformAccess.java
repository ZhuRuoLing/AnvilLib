package dev.anvilcraft.lib.v2.rendering.extension.blaze3d;

public interface NamedUniformAccess {
    int INVALID_UNIFORM_LOCATION = -1;

    int getUniformLocation(String name, ALRGpuDeviceBackendExtension device);
}
