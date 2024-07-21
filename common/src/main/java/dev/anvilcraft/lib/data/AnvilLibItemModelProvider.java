package dev.anvilcraft.lib.data;

import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

public class AnvilLibItemModelProvider implements DataProvider {
    @Override
    public @NotNull CompletableFuture<?> run(@NotNull CachedOutput output) {
        return CompletableFuture.allOf();
    }

    @Override
    public @NotNull String getName() {
        return "ItemModel";
    }
}
