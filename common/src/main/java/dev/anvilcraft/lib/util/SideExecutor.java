package dev.anvilcraft.lib.util;

import dev.architectury.injectables.annotations.ExpectPlatform;

import java.util.function.Supplier;

public class SideExecutor {
    @ExpectPlatform
    public static void execute(Side side, Supplier<Runnable> runnable) {
        throw new AssertionError();
    }
}
