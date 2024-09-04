package dev.anvilcraft.lib.test;

import dev.anvilcraft.lib.registrator.entry.BlockEntry;
import dev.anvilcraft.lib.registrator.entry.ItemEntry;
import dev.anvilcraft.lib.registrator.entry.RegistryEntry;
import dev.anvilcraft.lib.registrator.entry.TagKeyEntry;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
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
        .model((entry, provider) -> provider.simple(entry))
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
        .tag(TEST_BLOCK_TAG)
        .item()
        .model((entry, provider) -> {
        })
        .register();

    public static final RegistryEntry<CreativeModeTab> TEST_TAB = REGISTRATOR.tab("test", builder -> builder
            .title(Component.literal("Test Tab"))
            .icon(() -> new ItemStack(Items.APPLE))
            .displayItems((parameters, output) -> {
                output.accept(new ItemStack(TEST_ITEM));
                output.accept(new ItemStack(TEST_BLOCK));
            }))
        .register();

    public static void register() {
    }
}
