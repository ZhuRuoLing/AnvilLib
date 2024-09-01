package dev.anvilcraft.lib.data.provider;

import dev.anvilcraft.lib.data.file.BlockModelFile;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

public class AnvilLibBlockModelProvider extends ModelProvider<BlockModelFile> {

    public AnvilLibBlockModelProvider(
            Function<ResourceLocation, BlockModelFile> factory,
            String categoryDirectory,
            String modid,
            PackOutput output
    ) {
        super(factory, categoryDirectory, modid, output);
    }

    @Override
    public @NotNull CompletableFuture<?> run(@NotNull CachedOutput output) {
        return CompletableFuture.allOf();
    }

    @Override
    public @NotNull String getName() {
        return modid + "->BlockModel";
    }
}
