package dev.anvilcraft.lib.registrar;

import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public class ResourcePacksHelper {
    @ExpectPlatform
    public static void registerBuiltinResourcePack(@NotNull ResourceLocation pack, PackType type) {
        throw new AssertionError();
    }

    public enum PackType {
        CLIENT, SERVER, BOTH;

        public boolean isClient() {
            return this == CLIENT || this == BOTH;
        }

        public boolean isServer() {
            return this == SERVER || this == BOTH;
        }
    }
}
