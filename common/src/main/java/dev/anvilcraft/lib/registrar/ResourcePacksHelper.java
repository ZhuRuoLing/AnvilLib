package dev.anvilcraft.lib.registrar;

import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public class ResourcePacksHelper {
    @ExpectPlatform
    public static void registerBuiltinResourcePack(@NotNull ResourceLocation pack) {
        throw new AssertionError();
    }
}
