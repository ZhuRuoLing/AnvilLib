package dev.anvilcraft.lib.registrator.builder.forge;

import dev.anvilcraft.lib.registrator.AbstractRegistrator;
import dev.anvilcraft.lib.registrator.builder.BlockEntityBuilder;
import dev.anvilcraft.lib.util.TripleFunction;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
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
        NeoForge.EVENT_BUS.addListener(this::registerRenderers);
    }

    private void registerRenderers(FMLClientSetupEvent event) {
        if (this.renderer != null) {
            BlockEntityRenderers.register(this.entry().get(), this.renderer.get()::apply);
        }
    }
}
