package dev.anvilcraft.lib.v2.sync;

import dev.anvilcraft.lib.v2.sync.init.AnvilLibSyncEntries;
import dev.anvilcraft.lib.v2.sync.init.AnvilLibSyncRegistries;
import dev.anvilcraft.lib.v2.sync.management.SyncManager;
import lombok.extern.slf4j.Slf4j;
import net.minecraft.resources.Identifier;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.registries.RegisterEvent;

import java.util.Objects;

@Slf4j
@Mod(AnvilLibSync.MOD_ID)
public class AnvilLibSync {
    public static final String MOD_ID = "anvillib_sync";
    public static final SyncManager SYNC_MANAGER = new SyncManager();

    public AnvilLibSync(IEventBus modEventBus, ModContainer modContainer) {
        AnvilLibSyncEntries.SYNC_ENTRY.register(modEventBus);
        modEventBus.register(this);
    }

    public static Identifier of(String path) {
        return Identifier.fromNamespaceAndPath(MOD_ID, path);
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void onRegister(RegisterEvent event) {
        if (!Objects.equals(event.getRegistryKey(), AnvilLibSyncRegistries.SYNC_ENTRY)) return;
        AnvilLibSync.SYNC_MANAGER.compileContent();
    }
}
