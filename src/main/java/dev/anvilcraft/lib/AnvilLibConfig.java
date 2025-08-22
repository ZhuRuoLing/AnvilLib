package dev.anvilcraft.lib;

import dev.anvilcraft.lib.config.BoundedDiscrete;
import dev.anvilcraft.lib.config.Comment;
import dev.anvilcraft.lib.config.Config;

@Config(name = AnvilLib.MOD_ID)
public class AnvilLibConfig {
    @Comment("Maximum efficiency of in world recipes")
    @BoundedDiscrete(min = 1, max = 128)
    public int inWorldRecipeMaxEfficiency = 64;
}
