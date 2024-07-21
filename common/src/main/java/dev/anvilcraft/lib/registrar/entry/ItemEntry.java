package dev.anvilcraft.lib.registrar.entry;

import dev.anvilcraft.lib.registrar.builder.ItemBuilder;
import lombok.Getter;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;
import org.jetbrains.annotations.NotNull;

@Getter
public class ItemEntry<T extends Item> extends RegistryEntry<T> implements ItemLike {
    private final ItemBuilder<T> itemBuilder;

    public ItemEntry(ItemBuilder<T> itemBuilder) {
        this.itemBuilder = itemBuilder;
    }

    @Override
    public @NotNull T asItem() {
        return this.get();
    }
}
