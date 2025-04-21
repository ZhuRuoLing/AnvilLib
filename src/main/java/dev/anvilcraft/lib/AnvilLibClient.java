package dev.anvilcraft.lib;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLLoadCompleteEvent;
import org.jetbrains.annotations.NotNull;

@Mod(value = AnvilLib.MOD_ID, dist = Dist.CLIENT)
public class AnvilLibClient {
    public AnvilLibClient(@NotNull IEventBus modBus) {
        modBus.register(this);
    }

    @SubscribeEvent
    public void onClientSetup(@NotNull FMLLoadCompleteEvent event) {
        AnvilLib.getINTEGRATION_MANAGER().loadAllClientIntegrations(event.getClass());
    }
}
