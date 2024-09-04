package dev.anvilcraft.lib.util.fabric;

import dev.anvilcraft.lib.util.Side;
import net.fabricmc.api.EnvType;
import net.fabricmc.loader.api.FabricLoader;

import java.util.function.Supplier;

public class SideExecutorImpl {
    public static void execute(Side side, Supplier<Runnable> runnable) {
        if (FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT && side.isClient()) {
            runnable.get().run();
        } else if (FabricLoader.getInstance().getEnvironmentType() == EnvType.SERVER && side.isServer()) {
            runnable.get().run();
        }
    }
}
