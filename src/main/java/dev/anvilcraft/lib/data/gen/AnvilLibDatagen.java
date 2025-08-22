package dev.anvilcraft.lib.data.gen;

import dev.anvilcraft.lib.AnvilLib;
import dev.anvilcraft.lib.data.gen.provider.ModLanguageProvider;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.data.event.GatherDataEvent;

@EventBusSubscriber(modid = AnvilLib.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public class AnvilLibDatagen {
    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        PackOutput packOutput = generator.getPackOutput();
        generator.addProvider(event.includeClient(), new ModLanguageProvider(packOutput));
    }
}
