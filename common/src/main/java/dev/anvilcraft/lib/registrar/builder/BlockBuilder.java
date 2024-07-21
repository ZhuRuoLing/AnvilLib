package dev.anvilcraft.lib.registrar.builder;

import dev.anvilcraft.lib.data.AnvilLibBlockModelProvider;
import dev.anvilcraft.lib.data.DataProviderType;
import dev.anvilcraft.lib.registrar.AbstractRegistrar;
import dev.anvilcraft.lib.registrar.entry.BlockEntry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;

import java.util.function.BiConsumer;
import java.util.function.Function;

public class BlockBuilder<T extends Block> extends EntryBuilder<T> {
    private final BlockEntry<T> entry;
    private final Function<BlockBehaviour.Properties, T> factory;
    private final BlockBehaviour.Properties properties = BlockBehaviour.Properties.of();

    public BlockBuilder(AbstractRegistrar registrar, String id, Function<BlockBehaviour.Properties, T> factory) {
        super(registrar, id);
        this.factory = factory;
        this.entry = new BlockEntry<>(this);
    }

    public BlockBuilder<T> model(BiConsumer<BlockEntry<T>, AnvilLibBlockModelProvider> consumer) {
        this.registrar.data(DataProviderType.BLOCK_MODEL, p -> consumer.accept(this.entry, p));
        return this;
    }

    public T build() {
        T block = this.factory.apply(this.properties);
        this.entry.set(block);
        return block;
    }

    @Override
    public BlockEntry<T> register() {
        this.registrar.addBuilder(BuiltInRegistries.BLOCK, this);
        return this.entry;
    }
}
