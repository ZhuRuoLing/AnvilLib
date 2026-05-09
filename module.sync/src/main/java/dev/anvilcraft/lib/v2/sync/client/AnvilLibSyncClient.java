package dev.anvilcraft.lib.v2.sync.client;

import dev.anvilcraft.lib.v2.sync.AnvilLibSync;
import dev.anvilcraft.lib.v2.sync.management.SyncConfigManager;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.common.Mod;

@Mod(value = AnvilLibSync.MOD_ID, dist = Dist.CLIENT)
public class AnvilLibSyncClient {
    public static final SyncConfigManager SYNC_CONFIG_MANAGER = new SyncConfigManager();
}
