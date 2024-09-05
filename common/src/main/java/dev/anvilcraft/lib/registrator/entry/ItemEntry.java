package dev.anvilcraft.lib.registrator.entry;

import dev.anvilcraft.lib.registrator.builder.ItemBuilder;
import lombok.Getter;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import org.jetbrains.annotations.NotNull;

@Getter
public class ItemEntry<T extends Item> extends RegistryEntry<T> implements ItemLike {
    private final ItemBuilder<T, ?> itemBuilder;

    public ItemEntry(ItemBuilder<T, ?> itemBuilder) {
        this.itemBuilder = itemBuilder;
    }

    @Override
    public @NotNull T asItem() {
        return this.get();
    }

    public ItemStack asItemStack() {
        return new ItemStack(this);
    }

    public ItemStack asItemStack(int count) {
        return new ItemStack(this, count);
    }

    public boolean contains(ItemStack stack) {
        return is(stack.getItem());
    }

    public boolean is(Item item) {
        return asItem() == item;
    }
}
