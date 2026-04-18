package dev.anvilcraft.lib.v2.test.all;

import dev.anvilcraft.lib.v2.registrum.util.entry.BlockEntry;
import dev.anvilcraft.lib.v2.test.AnvilLibTest;
import dev.anvilcraft.lib.v2.test.block.TestBloomBlock;

public class TestBlocks {
    public static final BlockEntry<TestBloomBlock> TEST_BLOOM = AnvilLibTest.REGISTRUM
        .block("test_bloom", TestBloomBlock::new)
        .properties(p -> p.noOcclusion().noCollision())
        .simpleItem()
        .register();

    public static void setupRegistration() {
    }
}
