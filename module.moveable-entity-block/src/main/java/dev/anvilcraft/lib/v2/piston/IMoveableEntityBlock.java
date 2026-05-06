package dev.anvilcraft.lib.v2.piston;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;

@SuppressWarnings("unused")
public interface IMoveableEntityBlock extends EntityBlock {
    ///
    /// 在方块被推动时执行。
    /// 用于保存方块实体数据
    ///
    /// @param level  世界
    /// @param pos    方块位置
    /// @param output 数据输出
    ///
    default void storeData(Level level, BlockPos pos, ValueOutput output) {
    }

    ///
    /// 在方块停止时执行。
    /// 用于加载方块实体数据
    ///
    /// @param level 世界
    /// @param pos   方块位置
    /// @param input 数据输入
    ///
    default void loadData(Level level, BlockPos pos, ValueInput input) {
    }
}
