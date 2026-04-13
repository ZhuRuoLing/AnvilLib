package dev.anvilcraft.lib.v2.test;

import net.minecraft.resources.Identifier;
import net.neoforged.fml.common.Mod;

@Mod(AnvilLibTest.MOD_ID)
public class AnvilLibTest {
    public static final String MOD_ID = "anvillib_test";

    public static Identifier of(String path) {
        return Identifier.fromNamespaceAndPath(AnvilLibTest.MOD_ID, path);
    }
}
