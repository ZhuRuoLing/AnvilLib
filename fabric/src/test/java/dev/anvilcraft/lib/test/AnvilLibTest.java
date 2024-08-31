package dev.anvilcraft.lib.test;

import dev.anvilcraft.lib.registrator.Registrator;
import net.fabricmc.api.ModInitializer;

public class AnvilLibTest implements ModInitializer {
    public static final String MOD_ID = "anvil-lib-test";
    public static final Registrator REGISTRATOR = Registrator.create(MOD_ID);

    @Override
    public void onInitialize() {
        TestRegisters.register();
        REGISTRATOR.init();
    }
}
