package dev.anvilcraft.lib.registrator.builder;

import dev.anvilcraft.lib.registrator.AbstractRegistrator;
import net.minecraft.world.item.Item;

import java.util.function.Function;

public class RegistratorItemBuilder<T extends Item> extends ItemBuilder<T, RegistratorItemBuilder<T>>{
    public RegistratorItemBuilder(AbstractRegistrator registrator, String id, Function<Item.Properties, T> factory) {
        super(registrator, id, factory);
    }
}
