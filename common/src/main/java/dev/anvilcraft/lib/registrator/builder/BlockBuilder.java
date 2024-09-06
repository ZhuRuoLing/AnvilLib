package dev.anvilcraft.lib.registrator.builder;

import dev.anvilcraft.lib.data.DataProviderType;
import dev.anvilcraft.lib.data.provider.AnvilLibBlockStateProvider;
import dev.anvilcraft.lib.data.provider.BlockLootTableProvider;
import dev.anvilcraft.lib.data.provider.RegistratorRecipeProvider;
import dev.anvilcraft.lib.registrator.AbstractRegistrator;
import dev.anvilcraft.lib.registrator.entry.BlockEntry;
import dev.anvilcraft.lib.registrator.entry.ItemEntry;
import dev.anvilcraft.lib.registrator.entry.RegistryEntry;
import net.minecraft.Util;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;

import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

@SuppressWarnings("unused")
public class BlockBuilder<T extends Block> extends EntryBuilder<T> {
    private final BlockEntry<T> entry;
    private final Function<BlockBehaviour.Properties, T> factory;
    private Supplier<BlockBehaviour.Properties> propertiesSupplier = BlockBehaviour.Properties::of;
    private Consumer<BlockBehaviour.Properties> propertiesBuilder = properties -> {
    };
    private ItemBuilder<? extends BlockItem, ?> itemBuilder = new BlockItemBuilder<>(this.registrator, this, this.id, BlockItem::new);
    private Supplier<ItemEntry<?>> dropOther = null;
    private ItemEntry<? extends BlockItem> itemEntry = null;
    private BiConsumer<ItemEntry<? extends BlockItem>, RegistratorRecipeProvider> recipeFunction = null;

    public BlockBuilder(AbstractRegistrator registrator, String id, Function<BlockBehaviour.Properties, T> factory) {
        super(registrator, id);
        this.factory = factory;
        this.entry = new BlockEntry<>(this);
        this.defaultBlockState().lang(toTitleCase(this.id));
    }

    public BlockBuilder<T> state(BiConsumer<BlockEntry<T>, AnvilLibBlockStateProvider> consumer) {
        this.registrator.data(DataProviderType.BLOCK_STATE, p -> consumer.accept(this.entry, p));
        return this;
    }

    public BlockBuilder<T> initialProperties(BlockEntry<? extends Block> entry) {
        return initialProperties(entry::get);
    }

    public BlockBuilder<T> defaultBlockState(){
        return state((tBlockEntry, provider) -> provider.simpleBlock(tBlockEntry.get()));
    }

    public BlockBuilder<T> initialProperties(Supplier<Block> supplier) {
        propertiesSupplier = () -> BlockBehaviour.Properties.copy(supplier.get());
        return this;
    }

    public BlockBuilder<T> properties(Consumer<BlockBehaviour.Properties> propertiesBuilder) {
        this.propertiesBuilder = propertiesBuilder;
        return this;
    }

    @SafeVarargs
    public final BlockBuilder<T> tag(Supplier<TagKey<Block>>... tags) {
        this.registrator.data(DataProviderType.BLOCK_TAG, p -> {
            for (Supplier<TagKey<Block>> tag : tags) {
                p.add(tag.get(), this.entry);
            }
        });
        return this;
    }

    @SafeVarargs
    public final BlockBuilder<T> tag(TagKey<Block>... tags) {
        this.registrator.data(DataProviderType.BLOCK_TAG, p -> {
            for (TagKey<Block> tag : tags) {
                p.add(tag, this.entry);
            }
        });
        return this;
    }

    @SuppressWarnings("UnusedReturnValue")
    public BlockBuilder<T> defaultLoot() {
        return loot(BlockLootTableProvider::dropSelf);
    }

    public BlockBuilder<T> dropOther(Item item) {
        this.dropOther = null;
        return loot((prov, t) -> prov.dropOther(this.entry.get(), item));
    }

    public BlockBuilder<T> loot(BiConsumer<BlockLootTableProvider, T> cons) {
        this.registrator.data(DataProviderType.BLOCK_LOOT_TABLE, lt -> cons.accept(lt, this.entry.get()));
        return this;
    }

    @SuppressWarnings("UnusedReturnValue")
    public BlockBuilder<T> lang(String name) {
        this.registrator.lang(Util.makeDescriptionId("block", this.getId()), name);
        return this;
    }
    public BlockBuilder<T> defaultItem() {
        return this.blockItem().builder().defaultLoot();
    }

    public BlockItemBuilder<BlockItem, T> blockItem() {
        BlockItemBuilder<BlockItem, T> itemBuilder = item(BlockItem::new);
        this.itemBuilder = itemBuilder;
        this.defaultLoot();
        return itemBuilder;
    }

    public <I extends BlockItem> BlockItemBuilder<I, T> item(BiFunction<Block, Item.Properties, I> factory) {
        BlockItemBuilder<I, T> itemBuilder = new BlockItemBuilder<>(this.registrator, this, this.id, factory);
        if (recipeFunction != null){
            itemBuilder.recipe(recipeFunction::accept);
        }
        this.itemBuilder = itemBuilder;
        dropOther = () -> itemEntry;
        return itemBuilder;
    }

    public BlockBehaviour.Properties getBlockProperties() {
        BlockBehaviour.Properties prop = propertiesSupplier.get();
        propertiesBuilder.accept(prop);
        return prop;
    }

    public T build() {
        T block = this.factory.apply(getBlockProperties());
        this.entry.set(block);
        return block;
    }

    @Override
    public BlockEntry<T> register() {
        this.itemEntry = itemBuilder.register();
        this.entry.setBlockItem(itemEntry);
        this.registrator.addBuilder(BuiltInRegistries.BLOCK, this);
        if (dropOther != null) {
            this.loot((blockLootTableProvider, t) -> blockLootTableProvider.dropOther(this.entry.get(), dropOther.get()));
        }
        return this.entry;
    }

    public BlockBuilder<T> recipe(BiConsumer<ItemEntry<? extends BlockItem>, RegistratorRecipeProvider> fn) {
        itemBuilder.recipe(fn::accept);
        this.recipeFunction = fn;
        return this;
    }


    @Override
    public RegistryEntry<T> entry() {
        return this.entry;
    }
}
