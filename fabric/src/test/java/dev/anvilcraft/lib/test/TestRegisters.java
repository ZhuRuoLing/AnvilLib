package dev.anvilcraft.lib.test;

import dev.anvilcraft.lib.registrator.entry.BlockEntry;
import dev.anvilcraft.lib.registrator.entry.ItemEntry;
import dev.anvilcraft.lib.registrator.entry.TagKeyEntry;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;

import static dev.anvilcraft.lib.test.AnvilLibTest.REGISTRATOR;

public class TestRegisters {
    public static final TagKeyEntry<Item> TEST_ITEM_TAG = REGISTRATOR.tag(Registries.ITEM, "test");
    public static final TagKeyEntry<Block> TEST_BLOCK_TAG = REGISTRATOR.tag(Registries.BLOCK, "test");

    public static final ItemEntry<Item> TEST_ITEM = REGISTRATOR
            .item("test", Item::new)
            .tag(TEST_ITEM_TAG)
            .initProperties(() -> Items.APPLE)
            .model((entry, provider) -> {
            })
            .recipe((entry, provider) -> ShapedRecipeBuilder.shaped(RecipeCategory.MISC, entry)
                    .pattern("xxx")
                    .pattern("xxx")
                    .pattern("xxx")
                    .define('x', Items.APPLE)
                    .unlockedBy("has_apple", RecipeProvider.has(Items.APPLE))
                    .save(provider))
            .register();

    public static final BlockEntry<Block> TEST_BLOCK = REGISTRATOR
            .block("test_block", Block::new)
            .model((entry, provider) -> {
            })
            .register();

    public static void register() {
    }
}
