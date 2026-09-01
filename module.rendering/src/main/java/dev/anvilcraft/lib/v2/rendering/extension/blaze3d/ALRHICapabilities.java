package dev.anvilcraft.lib.v2.rendering.extension.blaze3d;

import com.mojang.blaze3d.systems.RenderSystem;

public record ALRHICapabilities(
    boolean compute,
    boolean bindlessTexturing,
    int maxImageUnit
) {

    public static ALRHICapabilities getInstance() {
        return ((ALRGpuDeviceExtension) RenderSystem.getDevice()).alrhiCreateCapabilities();
    }
}
