package dev.anvilcraft.lib.registrar.builder;

import dev.anvilcraft.lib.registrar.AbstractRegistrar;
import dev.anvilcraft.lib.registrar.entry.BlockEntry;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;

import java.util.function.Function;

public class BlockBuilder<B extends Block> extends EntryBuilder<B> {
    protected BlockBuilder(AbstractRegistrar registrar, String id, Function<BlockBehaviour.Properties, B> factory) {
        super(registrar, id);
    }

    @Override
    public BlockEntry<B> register() {
        return null;
    }
}
