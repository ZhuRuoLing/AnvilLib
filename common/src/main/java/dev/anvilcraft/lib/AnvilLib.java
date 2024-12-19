package dev.anvilcraft.lib;


import dev.anvilcraft.lib.util.Platform;
import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AnvilLib {
    public static final String MOD_ID = "anvillib";
    public static final String MOD_NAME = "AnvilLib";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_NAME);

    public static void init() {

    }

    public static @NotNull ResourceLocation of(@NotNull String path) {
        String namespace = MOD_ID;
        if (path.contains(":")) {
            namespace = path.split(":")[0];
            path = path.split(":")[1];
        }
        return new ResourceLocation(namespace, path);
    }

    @SuppressWarnings("unused")
    @ExpectPlatform
    public static Platform getPlatform() {
        throw new AssertionError();
    }

    @ExpectPlatform
    public static boolean isLoaded(String modid) {
        throw new AssertionError();
    }
}
