package dev.anvilcraft.lib.v2.piston.injection;

import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public interface IPistonMovingBlockEntityExtension {
    default @Nullable CompoundTag anvillib$clearData() {
        throw new AssertionError();
    }

    default void anvillib$setData(@Nullable CompoundTag nbt) {
        throw new AssertionError();
    }

    default @Nullable BlockState anvillib$getMoveState() {
        throw new AssertionError();
    }
}
