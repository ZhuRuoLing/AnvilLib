package dev.anvilcraft.lib;


import com.electronwill.nightconfig.core.UnmodifiableConfig;
import dev.anvilcraft.lib.integration.AnvilLibIntegrations;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModList;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforgespi.language.IModInfo;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;


@Mod(AnvilLib.MOD_ID)
public class AnvilLib {
    public static final String MOD_ID = "anvillib";
    public static final String MOD_NAME = "AnvilLib";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_NAME);

    public AnvilLib(IEventBus modEventBus) {
        for (IModInfo mod : ModList.get().getMods()) {
            Map<String, Object> modProperties = mod.getModProperties();
            for (Map.Entry<String, Object> entry : modProperties.entrySet()) {
                if (!entry.getKey().equals("anvilcraft")) continue;
                if (!(entry.getValue() instanceof UnmodifiableConfig anvilConfig)) return;
                for (UnmodifiableConfig.Entry entry1 : anvilConfig.entrySet()) {
                    if (!entry1.getKey().equals("integrations")) continue;
                    if (!(entry1.getValue() instanceof UnmodifiableConfig config)) return;
                    AnvilLib.loadIntegrations(config);
                }
            }
        }
        AnvilLibIntegrations.apply();
        modEventBus.addListener(AnvilLib::clientSetup);
    }

    public static void clientSetup(FMLClientSetupEvent event) {
        AnvilLibIntegrations.applyClient();
    }

    private static void loadIntegrations(@NotNull UnmodifiableConfig integrations) {
        for (UnmodifiableConfig.Entry entry2 : integrations.entrySet()) {
            String modid = entry2.getKey();
            Object value = entry2.getValue();
            List<String> classes = AnvilLibIntegrations.INTEGRATIONS.getOrDefault(modid, Collections.synchronizedList(new ArrayList<>()));
            if (value instanceof String string) {
                classes.add(string);
            } else if (value instanceof List<?> list) {
                list.stream()
                    .filter(i -> i instanceof String)
                    .map(Object::toString)
                    .forEach(classes::add);
            }
            AnvilLibIntegrations.INTEGRATIONS.put(modid, classes);
        }
    }

    public static boolean isLoaded(String modid) {
        return ModList.get().isLoaded(modid);
    }
}
