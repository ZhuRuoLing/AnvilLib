package dev.anvilcraft.lib;


import dev.anvilcraft.lib.init.LibItemSubPredicates;
import dev.anvilcraft.lib.init.reicpe.LibRecipeTypes;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;


@Mod(value = AnvilLib.MOD_ID, dist = Dist.CLIENT)
public class AnvilLibClient {

    public AnvilLibClient(IEventBus modEventBus,ModContainer modContainer) {
        AnvilLib.CONFIG_MANAGER.registerScreen(modContainer);
        LibItemSubPredicates.initialize(modEventBus);
        LibRecipeTypes.register(modEventBus);
    }
}
