package dev.anvilcraft.lib.v2.piston.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import dev.anvilcraft.lib.v2.piston.IMoveableEntityBlock;
import dev.anvilcraft.lib.v2.piston.injection.IPistonMovingBlockEntityExtension;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.piston.PistonMovingBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.annotation.ParametersAreNonnullByDefault;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
@Mixin(PistonMovingBlockEntity.class)
abstract class PistonMovingBlockEntityMixin extends BlockEntity implements IPistonMovingBlockEntityExtension {
    @Shadow
    private BlockState movedState;
    @Unique
    private CompoundTag anvillib$nbt = new CompoundTag();

    public PistonMovingBlockEntityMixin(BlockEntityType<?> type, BlockPos pos, BlockState blockState) {
        super(type, pos, blockState);
    }

    @Override
    public CompoundTag anvillib$clearData() {
        CompoundTag nbt = this.anvillib$nbt;
        this.anvillib$nbt = new CompoundTag();
        return nbt;
    }

    @Override
    public void anvillib$setData(@Nullable CompoundTag nbt) {
        if (nbt == null) return;
        this.anvillib$nbt.merge(nbt);
    }

    @Override
    public BlockState anvillib$getMoveState() {
        return this.movedState;
    }

    @Inject(
        method = "tick",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/level/Level;"
                     + "setBlock(Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;I)Z",
            shift = At.Shift.AFTER,
            ordinal = 1
        )
    )
    private static void tick(
        Level level,
        BlockPos pos,
        BlockState state,
        PistonMovingBlockEntity blockEntity,
        CallbackInfo ci,
        @Local(ordinal = 1) BlockState moveState
    ) {
        if (level.isClientSide()) return;
        if (!(blockEntity instanceof IPistonMovingBlockEntityExtension blockEntity1)) return;
        if (!(moveState.getBlock() instanceof IMoveableEntityBlock entityBlock)) return;
        CompoundTag tag = blockEntity1.anvillib$clearData();
        if (tag != null) {
            entityBlock.setData(level, pos, tag);
        }
    }

    @Inject(
        method = "finalTick", at = @At(
        value = "INVOKE",
        target = "Lnet/minecraft/world/level/Level;"
                 + "setBlock(Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;I)Z",
        shift = At.Shift.AFTER
    )
    )
    private void finalTick(CallbackInfo ci, @Local BlockState moveState) {
        if (this.level == null || this.level.isClientSide()) return;
        // noinspection ConstantValue
        if (!(this instanceof IPistonMovingBlockEntityExtension blockEntity1)) return;
        if (!(moveState.getBlock() instanceof IMoveableEntityBlock entityBlock)) return;
        CompoundTag tag = blockEntity1.anvillib$clearData();
        // noinspection ConstantValue
        if (tag != null) {
            entityBlock.setData(level, this.worldPosition, tag);
        }
    }
}
