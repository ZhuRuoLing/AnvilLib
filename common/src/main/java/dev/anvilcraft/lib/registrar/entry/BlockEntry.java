package dev.anvilcraft.lib.registrar.entry;

import dev.anvilcraft.lib.registrar.builder.BlockBuilder;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.NotNull;

public class BlockEntry<B extends Block> extends RegistryEntry<B> implements ItemLike {
    private final BlockBuilder<B> blockBuilder;
    private ItemEntry<? extends BlockItem> blockItem = null;

    public BlockEntry(BlockBuilder<B> blockBuilder) {
        this.blockBuilder = blockBuilder;
    }

    @Override
    public @NotNull Item asItem() {
        if (blockItem == null) return Items.AIR;
        return blockItem.asItem();
    }

    public void setBlockItem(ItemEntry<? extends BlockItem> blockItem) {
        if (this.blockItem != null) throw new RuntimeException();
        this.blockItem = blockItem;
    }
}
