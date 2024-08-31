package dev.anvilcraft.lib.data;

import net.minecraft.data.DataProvider;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

public interface DataProviderType<P extends DataProvider> {
    DataProviderType<AnvilLibItemModelProvider> ITEM_MODEL = new DataProviderType<>() {
    };
    DataProviderType<AnvilLibBlockModelProvider> BLOCK_MODEL = new DataProviderType<>() {
    };
    DataProviderType<RegistratorRecipeProvider> RECIPE = new DataProviderType<>() {
    };
    DataProviderType<TagsProvider<Item>> ITEM_TAG = new DataProviderType<>() {
    };
    DataProviderType<TagsProvider<Block>> BLOCK_TAG = new DataProviderType<>() {
    };
    DataProviderType<LanguageProvider> LANG = new DataProviderType<>() {
    };
}
