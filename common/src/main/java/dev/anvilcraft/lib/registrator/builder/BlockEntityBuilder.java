package dev.anvilcraft.lib.registrator.builder;

import dev.anvilcraft.lib.mixin.BlockEntityRenderersAccessor;
import dev.anvilcraft.lib.registrator.AbstractRegistrator;
import dev.anvilcraft.lib.registrator.entry.RegistryEntry;
import dev.anvilcraft.lib.util.Side;
import dev.anvilcraft.lib.util.SideExecutor;
import dev.anvilcraft.lib.util.TripleFunction;
import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public abstract class BlockEntityBuilder<T extends BlockEntity> extends EntryBuilder<BlockEntityType<T>> {
    protected final RegistryEntry<BlockEntityType<T>> entry = new RegistryEntry<>() {
    };
    protected final TripleFunction<BlockEntityType<T>, BlockPos, BlockState, T> factory;
    protected Consumer<BlockEntityType<T>> onRegister = t -> {
    };
    protected List<Supplier<? extends Block>> blocks = new ArrayList<>();
    protected Supplier<Function<BlockEntityRendererProvider.Context, BlockEntityRenderer<? super T>>> renderer = null;

    protected BlockEntityBuilder(AbstractRegistrator registrator, String id, TripleFunction<BlockEntityType<T>, BlockPos, BlockState, T> factory) {
        super(registrator, id);
        this.factory = factory;
    }

    @ExpectPlatform
    public static <T extends BlockEntity> BlockEntityBuilder<T> create(AbstractRegistrator registrator, String id, TripleFunction<BlockEntityType<T>, BlockPos, BlockState, T> factory) {
        throw new AssertionError();
    }

    @SuppressWarnings("UnusedReturnValue")
    public BlockEntityBuilder<T> onRegister(Consumer<BlockEntityType<T>> consumer) {
        this.onRegister = consumer;
        return this;
    }

    @SafeVarargs
    public final BlockEntityBuilder<T> validBlock(Supplier<? extends Block>... block) {
        this.blocks.addAll(List.of(block));
        return this;
    }

    public BlockEntityBuilder<T> renderer(Supplier<Function<BlockEntityRendererProvider.Context, BlockEntityRenderer<? super T>>> renderer) {
        if (this.renderer == null) SideExecutor.execute(Side.CLIENT, () -> this::registerRenderer);
        this.renderer = renderer;
        return this;
    }

    protected abstract void registerRenderer();

    @Override
    public BlockEntityType<T> build() {
        //noinspection DataFlowIssue
        BlockEntityType<T> type = BlockEntityType.Builder
            .of(
                (pos, state) -> this.factory.apply(this.entry.get(), pos, state),
                this.blocks.stream().map(Supplier::get).toArray(Block[]::new)
            )
            .build(null);
        this.entry.set(type);
        this.onRegister.accept(type);
        return type;
    }

    @Override
    public RegistryEntry<BlockEntityType<T>> register() {
        this.registrator.addBuilder(BuiltInRegistries.BLOCK_ENTITY_TYPE, this);
        return this.entry;
    }

    @Override
    public RegistryEntry<BlockEntityType<T>> entry() {
        return this.entry;
    }
}
