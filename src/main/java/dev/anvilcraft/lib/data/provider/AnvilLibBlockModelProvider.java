package dev.anvilcraft.lib.data.provider;

import dev.anvilcraft.lib.data.file.BlockModelFile;
import net.minecraft.data.PackOutput;

public class AnvilLibBlockModelProvider extends ModelProvider<BlockModelFile> {

    public AnvilLibBlockModelProvider(
            String categoryDirectory,
            String modid,
            PackOutput output
    ) {
        super(BlockModelFile::new, categoryDirectory, modid, output);
    }

    @Override
    String getProviderName() {
        return "BlockModel";
    }
}
