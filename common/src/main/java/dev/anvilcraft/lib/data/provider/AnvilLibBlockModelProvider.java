package dev.anvilcraft.lib.data.provider;

import dev.anvilcraft.lib.data.file.BlockModelFile;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.PackOutput;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

public class AnvilLibBlockModelProvider extends ModelProvider<BlockModelFile> {

    public AnvilLibBlockModelProvider(
            String categoryDirectory,
            String modid,
            PackOutput output
    ) {
        super(BlockModelFile::new, categoryDirectory, modid, output);
    }

    @Override
    public @NotNull CompletableFuture<?> run(@NotNull CachedOutput output) {
        return CompletableFuture.allOf();
    }

    @Override
    String getProviderName() {
        return "BlockModel";
    }
}
