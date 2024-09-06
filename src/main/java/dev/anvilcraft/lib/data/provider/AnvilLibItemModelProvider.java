package dev.anvilcraft.lib.data.provider;

import dev.anvilcraft.lib.data.file.ItemModelFile;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.ItemLike;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

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

    public ResourceLocation itemTexture(Supplier<? extends ItemLike> item) {
        return modLocation("item/" + name(item.get()));
    }

    public ItemModelFile simple(@NotNull ItemLike item) {
        return simple(BuiltInRegistries.ITEM.getKey(item.asItem()));
    }

    public ItemModelFile simple(ResourceLocation location) {
        return getBuilder(location)
            .parent(ResourceLocation.withDefaultNamespace("item/generated"))
            .texture("layer0", ResourceLocation.fromNamespaceAndPath(location.getNamespace(), "item/" + location.getPath()));
    }

    public String modid(ItemLike item) {
        return BuiltInRegistries.ITEM.getKey(item.asItem()).getNamespace();
    }

    public String name(ItemLike item) {
        return BuiltInRegistries.ITEM.getKey(item.asItem()).getPath();
    }

    public ItemModelFile blockItem(@NotNull ItemLike item) {
        return blockItem(item, "");
    }

    public ItemModelFile blockItem(@NotNull ItemLike item, String suffix) {
        if (item.asItem() == null) return null;
        return getBuilder(BuiltInRegistries.ITEM.getKey(item.asItem()))
            .parent(ResourceLocation.fromNamespaceAndPath(modid(item), "block/" + name(item) + suffix));
    }

    public ItemModelFile handheld(Supplier<? extends ItemLike> itemSupplier) {
        return handheld(itemSupplier, itemTexture(itemSupplier));
    }

    public ItemModelFile handheld(Supplier<? extends ItemLike> item, ResourceLocation texture) {
        return getBuilder(name(item.get()))
            .parent(mcLocation("item/handheld"))
            .texture("layer0", texture);
    }
}
