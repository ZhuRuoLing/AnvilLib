package dev.anvilcraft.lib.v2.sync;

import dev.anvilcraft.lib.v2.sync.init.AnvilLibSyncEntries;
import dev.anvilcraft.lib.v2.sync.init.AnvilLibSyncRegistries;
import dev.anvilcraft.lib.v2.sync.management.SyncConfigManager;
import dev.anvilcraft.lib.v2.sync.management.SyncManager;
import lombok.extern.slf4j.Slf4j;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.network.protocol.configuration.ServerConfigurationPacketListener;
import net.minecraft.resources.Identifier;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.network.configuration.ICustomConfigurationTask;
import net.neoforged.neoforge.network.event.RegisterConfigurationTasksEvent;
import net.neoforged.neoforge.registries.RegisterEvent;

import java.util.Objects;
import java.util.function.Consumer;

@Slf4j
@Mod(AnvilLibSync.MOD_ID)
public class AnvilLibSync {
    public static final String MOD_ID = "anvillib_sync";
    public static final SyncManager SYNC_MANAGER = new SyncManager();
    public static final SyncConfigManager SYNC_CONFIG_MANAGER = new SyncConfigManager();

    public AnvilLibSync(IEventBus modEventBus, ModContainer modContainer) {
        AnvilLibSyncEntries.SYNC_ENTRY.register(modEventBus);
        modEventBus.register(this);
    }

    @SubscribeEvent
    public void onRegister(FMLCommonSetupEvent event) {
        SyncConfigManager.compileContent();
    }

    public static Identifier of(String path) {
        return Identifier.fromNamespaceAndPath(MOD_ID, path);
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void onRegister(RegisterEvent event) {
        if (!Objects.equals(event.getRegistryKey(), AnvilLibSyncRegistries.SYNC_ENTRY)) return;
        AnvilLibSync.SYNC_MANAGER.compileContent();
    }

    @SubscribeEvent
    public void onRegisterConfigurationTasks(RegisterConfigurationTasksEvent event) {
        event.register(new SyncConfig(event.getListener()));
    }

    public record SyncConfig(ServerConfigurationPacketListener listener) implements ICustomConfigurationTask {
        public static Type TYPE = new Type(AnvilLibSync.of("sync_config"));

        @Override
        public void run(Consumer<CustomPacketPayload> sender) {
            sender.accept(AnvilLibSync.SYNC_CONFIG_MANAGER.createPyload());
        }

        @Override
        public Type type() {
            return SyncConfig.TYPE;
        }
    }
}
