package dev.anvilcraft.lib;


import dev.anvilcraft.lib.config.ConfigManager;
import dev.anvilcraft.lib.init.LibItemSubPredicates;
import dev.anvilcraft.lib.init.reicpe.LibRecipeInits;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;


@Mod(AnvilLib.MOD_ID)
public class AnvilLib {
    public static final String MOD_ID = "anvillib";
    public static final AnvilLibConfig CONFIG = ConfigManager.register(AnvilLib.MOD_ID, AnvilLibConfig::new);

    public AnvilLib(IEventBus modEventBus, ModContainer modContainer) {
        LibItemSubPredicates.initialize(modEventBus);
        LibRecipeInits.init(modEventBus);
    }

    public static ResourceLocation of(String path) {
        return ResourceLocation.fromNamespaceAndPath(MOD_ID, path);
    }
}
