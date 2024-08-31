package dev.anvilcraft.lib.test;

import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;

public class AnvilLibTestDataGenerator implements DataGeneratorEntrypoint {
    @Override
    public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
        AnvilLibTest.REGISTRATOR.initDatagen(fabricDataGenerator);
    }
}
