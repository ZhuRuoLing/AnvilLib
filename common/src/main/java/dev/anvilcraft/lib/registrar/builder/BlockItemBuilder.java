package dev.anvilcraft.lib.registrar.builder;

import dev.anvilcraft.lib.registrar.AbstractRegistrar;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

import java.util.function.Function;

public class BlockItemBuilder<T extends BlockItem, B extends Block> extends ItemBuilder<T> {
    protected final BlockBuilder<B> blockBuilder;

    public BlockItemBuilder(AbstractRegistrar registrar, BlockBuilder<B> builder, String id, Function<Item.Properties, T> factory) {
        super(registrar, id, factory);
        this.blockBuilder = builder;
    }

    public BlockBuilder<B> builder() {
        this.register();
        return this.blockBuilder;
    }
}
