package dev.anvilcraft.lib.util;

import dev.anvilcraft.lib.util.forge.SideExecutorImpl;

import java.util.function.Supplier;

public class SideExecutor {
    public static void execute(Side side, Supplier<Runnable> runnable) {
        SideExecutorImpl.execute(side,runnable);
    }
}
