package dev.anvilcraft.lib.registrator.builder;

import dev.anvilcraft.lib.data.AnvilLibBlockModelProvider;
import dev.anvilcraft.lib.data.DataProviderType;
import dev.anvilcraft.lib.registrator.AbstractRegistrator;
import dev.anvilcraft.lib.registrator.entry.BlockEntry;
import dev.anvilcraft.lib.registrator.entry.ItemEntry;
import net.minecraft.Util;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;

import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

public class BlockBuilder<T extends Block> extends EntryBuilder<T> {
    private final BlockEntry<T> entry;
    private final Function<BlockBehaviour.Properties, T> factory;
    private final BlockBehaviour.Properties properties = BlockBehaviour.Properties.of();

    public BlockBuilder(AbstractRegistrator registrator, String id, Function<BlockBehaviour.Properties, T> factory) {
        super(registrator, id);
        this.factory = factory;
        this.entry = new BlockEntry<>(this);
        this.lang(toTitleCase(this.id));
    }

    void setEntryBlockItem(ItemEntry<? extends BlockItem> entry) {
        this.entry.setBlockItem(entry);
    }

    public BlockBuilder<T> model(BiConsumer<BlockEntry<T>, AnvilLibBlockModelProvider> consumer) {
        this.registrator.data(DataProviderType.BLOCK_MODEL, p -> consumer.accept(this.entry, p));
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
    public BlockBuilder<T> lang(String name) {
        this.registrator.lang(Util.makeDescriptionId("block", this.registrator.of(this.id)), name);
        return this;
    }

    public BlockItemBuilder<BlockItem, T> item() {
        return new BlockItemBuilder<>(this.registrator, this, this.id, BlockItem::new);
    }

    public <I extends BlockItem> BlockItemBuilder<I, T> item(BiFunction<Block, Item.Properties, I> factory) {
        return new BlockItemBuilder<>(this.registrator, this, this.id, factory);
    }

    public T build() {
        T block = this.factory.apply(this.properties);
        this.entry.set(block);
        return block;
    }

    @Override
    public BlockEntry<T> register() {
        this.registrator.addBuilder(BuiltInRegistries.BLOCK, this);
        return this.entry;
    }
}
