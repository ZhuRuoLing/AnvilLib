package dev.anvilcraft.lib.registrator;

import dev.anvilcraft.lib.registrator.forge.ResourcePacksHelperImpl;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public class ResourcePacksHelper {
    public static void registerBuiltinResourcePack(@NotNull ResourceLocation pack, PackType type) {
        ResourcePacksHelperImpl.registerBuiltinResourcePack(pack,type);
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
