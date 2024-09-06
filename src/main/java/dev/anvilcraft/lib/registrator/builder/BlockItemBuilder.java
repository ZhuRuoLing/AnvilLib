package dev.anvilcraft.lib.registrator.builder;

import dev.anvilcraft.lib.registrator.AbstractRegistrator;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

import java.util.function.BiFunction;

public class BlockItemBuilder<T extends BlockItem, B extends Block> extends ItemBuilder<T, BlockItemBuilder<T, B>> {
    protected final BlockBuilder<B> blockBuilder;

    public BlockItemBuilder(AbstractRegistrator registrator, BlockBuilder<B> builder, String id, BiFunction<Block, Item.Properties, T> factory) {
        super(registrator, id, (properties) -> factory.apply(builder.entry().get(), properties));
        this.blockBuilder = builder;
        this.lang(null);
        this.defaultBlockItemModel();
    }

    public BlockItemBuilder<T,B> defaultBlockItemModel(){
        return model((itemEntry1, modelProvider) -> modelProvider.blockItem(itemEntry1));
    }

    public BlockBuilder<B> builder() {
        return this.blockBuilder;
    }
}
