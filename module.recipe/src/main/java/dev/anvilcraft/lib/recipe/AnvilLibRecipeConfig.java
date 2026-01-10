package dev.anvilcraft.lib.recipe;

import dev.anvilcraft.lib.config.BoundedDiscrete;
import dev.anvilcraft.lib.config.Comment;
import dev.anvilcraft.lib.config.Config;

@Config(name = AnvilLibRecipe.MOD_ID)
public class AnvilLibRecipeConfig {
    @Comment("Maximum efficiency of in world recipes")
    @BoundedDiscrete(min = 1, max = 128)
    public int inWorldRecipeMaxEfficiency = 64;
}
