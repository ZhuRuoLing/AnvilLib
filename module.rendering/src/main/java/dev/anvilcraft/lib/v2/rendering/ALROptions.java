package dev.anvilcraft.lib.v2.rendering;

import org.jspecify.annotations.Nullable;

@SuppressWarnings("SameParameterValue")
public class ALROptions {
    public static final boolean SPD_OPTION_WAVE_INTEROP_LDS = getPropertyBoolean("alrSpdOptionUseWaveInteropLds");
    public static final String OCCLUSION_CULLING_FORCE_IMPL = getProperty("alrOcclusionCullingForceImplementation", null);

    private static String getProperty(String key, @Nullable String defaultValue) {
        return System.getProperty(key, defaultValue);
    }

    private static boolean getPropertyBoolean(String key) {
        String prop = System.getProperty(key);
        return !"false".equals(prop);
    }
}
