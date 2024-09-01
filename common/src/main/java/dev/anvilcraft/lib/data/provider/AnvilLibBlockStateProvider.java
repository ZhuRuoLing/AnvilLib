package dev.anvilcraft.lib.data.provider;

import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;

import java.util.concurrent.CompletableFuture;

public class AnvilLibBlockStateProvider implements DataProvider {

    @Override
    public CompletableFuture<?> run(CachedOutput output) {
        return null;
    }

    @Override
    public String getName() {
        return "";
    }
}
