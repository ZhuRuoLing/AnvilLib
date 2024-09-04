package dev.anvilcraft.lib.data.provider;

import dev.anvilcraft.lib.data.file.ItemModelFile;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.ItemLike;
import org.jetbrains.annotations.NotNull;

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

    public ItemModelFile simple(@NotNull ItemLike item) {
        return simple(BuiltInRegistries.ITEM.getKey(item.asItem()));
    }

    public ItemModelFile simple(ResourceLocation location) {
        return getBuilder(location)
                .parent(new ResourceLocation("item/generated"))
                .texture("layer0", new ResourceLocation(location.getNamespace(), "item/" + location.getPath()));
    }

    public String modid(ItemLike item) {
        return BuiltInRegistries.ITEM.getKey(item.asItem()).getNamespace();
    }

    public String name(ItemLike item) {
        return BuiltInRegistries.ITEM.getKey(item.asItem()).getPath();
    }

    public ItemModelFile blockItem(@NotNull ItemLike item){
        return blockItem(item, "");
    }

    public ItemModelFile blockItem(@NotNull ItemLike item, String suffix){
        return getBuilder(BuiltInRegistries.ITEM.getKey(item.asItem()))
                .parent(new ResourceLocation(modid(item), "block/" + name(item) + suffix));
    }
}
