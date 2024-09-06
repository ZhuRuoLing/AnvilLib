package dev.anvilcraft.lib;


import dev.anvilcraft.lib.forge.AnvilLibImpl;
import dev.anvilcraft.lib.util.Platform;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AnvilLib {
    public static final String MOD_ID = "anvillib";
    public static final String MOD_NAME = "AnvilLib";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_NAME);

    public static void init() {

    }

    public static Platform getPlatform() {
        return AnvilLibImpl.getPlatform();
    }

    public static boolean isLoaded(String modid) {
        return AnvilLibImpl.isLoaded(modid);
    }
}
