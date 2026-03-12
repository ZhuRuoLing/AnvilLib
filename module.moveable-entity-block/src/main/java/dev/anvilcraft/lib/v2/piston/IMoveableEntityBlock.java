package dev.anvilcraft.lib.v2.piston;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.EntityBlock;

@SuppressWarnings("unused")
public interface IMoveableEntityBlock extends EntityBlock {
    /**
     * 在可推动方块实体方块被推动时所会执行的，它的返回值是需要传递的方块实体数据
     * @param level 世界
     * @param pos 方块位置
     * @return 需要被传递的方块实体数据
     */
    default CompoundTag clearData(Level level, BlockPos pos) {
        return new CompoundTag();
    }

    /**
     * 在在可推动方块实体方块抵达被推动的位置停下时，将被传递的方块实体数据重新设置进入方块实体的方法
     * @param level 世界
     * @param pos 方块位置
     * @param nbt 需要被设置的方块实体数据
     */
    default void setData(Level level, BlockPos pos, CompoundTag nbt) {
    }
}
