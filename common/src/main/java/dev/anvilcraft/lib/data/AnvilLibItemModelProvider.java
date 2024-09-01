package dev.anvilcraft.lib.data;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;

public class AnvilLibItemModelProvider extends ModelProvider<ItemModelFile> {
    public AnvilLibItemModelProvider(
            Function<ResourceLocation, ItemModelFile> factory,
            String categoryDirectory,
            String modid,
            PackOutput output
    ) {
        super(factory, categoryDirectory, modid, output);
    }

    public ItemModelFile simple(Item item) {
        return simple(BuiltInRegistries.ITEM.getKey(item));
    }

    public ItemModelFile simple(ResourceLocation location) {
        return getBuilder(location)
                .parent(new ResourceLocation("item/generated"))
                .texture("layer0", new ResourceLocation(location.getNamespace(), "item/" + location.getPath()));
    }

    @Override
    public @NotNull String getName() {
        return modid + "->ItemModel";
    }
}
