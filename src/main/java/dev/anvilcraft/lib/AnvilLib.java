package dev.anvilcraft.lib;


import dev.anvilcraft.lib.integration.IntegrationManager;
import lombok.Getter;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModList;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLLoadCompleteEvent;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@Mod(AnvilLib.MOD_ID)
public class AnvilLib {
    public static final String MOD_ID = "anvillib";
    public static final String MOD_NAME = "AnvilLib";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_NAME);
    @Getter
    private static final IntegrationManager INTEGRATION_MANAGER = new IntegrationManager();

    public AnvilLib(@NotNull IEventBus modEventBus) {
        modEventBus.register(this);
    }

    @SubscribeEvent
    public void loadComplete(FMLLoadCompleteEvent event) {
        AnvilLib.INTEGRATION_MANAGER.compileContent();
        AnvilLib.INTEGRATION_MANAGER.loadAllIntegrations();
    }

    public static boolean isLoaded(String modid) {
        return ModList.get().isLoaded(modid);
    }
}
