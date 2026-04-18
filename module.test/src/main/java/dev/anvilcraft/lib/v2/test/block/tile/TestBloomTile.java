package dev.anvilcraft.lib.v2.test.block.tile;

import dev.anvilcraft.lib.v2.test.all.TestTiles;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class TestBloomTile extends BlockEntity {
    public TestBloomTile(BlockEntityType<?> type, BlockPos worldPosition, BlockState blockState) {
        super(type, worldPosition, blockState);
    }

    public TestBloomTile(BlockPos worldPosition, BlockState blockState) {
        super(TestTiles.TEST_BLOOM.get(), worldPosition, blockState);
    }
}
