package dev.anvilcraft.lib.v2.rendering;

public class ALROptions {
    public static final boolean SPD_OPTION_WAVE_INTEROP_LDS = getPropertyBoolean("alrSpdOptionUseWaveInteropLds");


    private static boolean getPropertyBoolean(String key) {
        String prop = System.getProperty(key);
        return !"false".equals(prop);
    }
}
