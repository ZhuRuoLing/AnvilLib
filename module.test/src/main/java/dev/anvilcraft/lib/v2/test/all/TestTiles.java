package dev.anvilcraft.lib.v2.test.all;

import dev.anvilcraft.lib.v2.registrum.util.entry.BlockEntityEntry;
import dev.anvilcraft.lib.v2.test.AnvilLibTest;
import dev.anvilcraft.lib.v2.test.block.tile.TestBloomTile;
import dev.anvilcraft.lib.v2.test.client.tesr.TestBloomTESR;

public class TestTiles {
    public static final BlockEntityEntry<TestBloomTile> TEST_BLOOM = AnvilLibTest.REGISTRUM
        .<TestBloomTile>blockEntity("test_bloom", TestBloomTile::new)
        .validBlock(TestBlocks.TEST_BLOOM)
        .renderer(() -> TestBloomTESR::new)
        .register();

    public static void setupRegistration() {
    }
}
