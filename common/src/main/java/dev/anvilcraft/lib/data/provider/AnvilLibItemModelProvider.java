package dev.anvilcraft.lib.data.provider;

import dev.anvilcraft.lib.data.file.ItemModelFile;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;

public class AnvilLibItemModelProvider extends ModelProvider<ItemModelFile> {
    public AnvilLibItemModelProvider(
            String categoryDirectory,
            String modid,
            PackOutput output
    ) {
        super(ItemModelFile::new, categoryDirectory, modid, output);
    }

    @Override
    String getProviderName() {
        return "ItemModel";
    }

    public ItemModelFile simple(Item item) {
        return simple(BuiltInRegistries.ITEM.getKey(item));
    }

    public ItemModelFile simple(ResourceLocation location) {
        return getBuilder(location)
                .parent(new ResourceLocation("item/generated"))
                .texture("layer0", new ResourceLocation(location.getNamespace(), "item/" + location.getPath()));
    }
}
