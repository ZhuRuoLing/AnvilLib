package dev.anvilcraft.lib.registrator.entry;

import dev.anvilcraft.lib.registrator.builder.BlockBuilder;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.NotNull;

public class BlockEntry<T extends Block> extends RegistryEntry<T> implements ItemLike {
    private final BlockBuilder<T> blockBuilder;
    private ItemEntry<? extends BlockItem> blockItem = null;

    public BlockEntry(BlockBuilder<T> blockBuilder) {
        this.blockBuilder = blockBuilder;
    }

    @Override
    public @NotNull Item asItem() {
        if (blockItem == null) {
            throw new RuntimeException("Block %s is not has item!".formatted(blockBuilder.getId()));
        }
        return blockItem.asItem();
    }

    public void setBlockItem(ItemEntry<? extends BlockItem> blockItem) {
        if (this.blockItem != null) throw new RuntimeException();
        this.blockItem = blockItem;
    }
}
