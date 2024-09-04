package dev.anvilcraft.lib.util.forge;

import dev.anvilcraft.lib.util.Side;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.loading.FMLEnvironment;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

public class SideExecutorImpl {
    @OnlyIn(Dist.CLIENT)
    public static void execute(@NotNull Side side, Supplier<Runnable> runnable) {
        if (side.isClient() && FMLEnvironment.dist.isClient()) runnable.get().run();
        else if (side.isServer() && FMLEnvironment.dist.isDedicatedServer()) runnable.get().run();
    }
}
