package dev.anvilcraft.lib.registrator.builder.fabric;

import dev.anvilcraft.lib.registrator.AbstractRegistrator;
import dev.anvilcraft.lib.registrator.builder.CreativeModeTabBuilder;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.world.item.CreativeModeTab;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

public class CreativeModeTabBuilderImpl extends CreativeModeTabBuilder {
    protected CreativeModeTabBuilderImpl(AbstractRegistrator registrator, String id) {
        super(registrator, id, FabricItemGroup.builder());
    }

    public static @NotNull CreativeModeTabBuilder create(AbstractRegistrator registrator, String id, @NotNull Consumer<CreativeModeTab.Builder> consumer) {
        CreativeModeTabBuilderImpl builder = new CreativeModeTabBuilderImpl(registrator, id);
        consumer.accept(builder.builder);
        return builder;
    }
}
