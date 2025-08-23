package dev.anvilcraft.lib;


import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;


@Mod(value = AnvilLib.MOD_ID, dist = Dist.CLIENT)
public class AnvilLibClient {

    public AnvilLibClient(ModContainer modContainer) {
        AnvilLib.CONFIG_MANAGER.registerScreen(modContainer);
    }
}
