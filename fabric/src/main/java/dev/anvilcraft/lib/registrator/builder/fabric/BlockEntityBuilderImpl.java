package dev.anvilcraft.lib.registrator.builder.fabric;

import dev.anvilcraft.lib.mixin.BlockEntityRenderersAccessor;
import dev.anvilcraft.lib.registrator.AbstractRegistrator;
import dev.anvilcraft.lib.registrator.builder.BlockEntityBuilder;
import dev.anvilcraft.lib.util.TripleFunction;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;


public class BlockEntityBuilderImpl<T extends BlockEntity> extends BlockEntityBuilder<T> {
    protected BlockEntityBuilderImpl(AbstractRegistrator registrator, String id, TripleFunction<BlockEntityType<T>, BlockPos, BlockState, T> factory) {
        super(registrator, id, factory);
    }

    public static <T extends BlockEntity> @NotNull BlockEntityBuilder<T> create(AbstractRegistrator registrator, String id, TripleFunction<BlockEntityType<T>, BlockPos, BlockState, T> factory) {
        return new BlockEntityBuilderImpl<>(registrator, id, factory);
    }

    @Override
    protected void registerRenderer() {
        this.onRegister(entry -> BlockEntityRenderersAccessor.invokeRegister(entry, renderer.get()::apply));
    }
}
