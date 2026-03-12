package dev.anvilcraft.lib.v2.recipe.injection;

import dev.anvilcraft.lib.v2.recipe.InWorldRecipe;
import net.minecraft.world.item.crafting.RecipeHolder;

import java.util.List;

public interface IRecipeMapExtension {
    default void anvillib$addRecipes(List<RecipeHolder<InWorldRecipe>> recipes) {
        throw new AssertionError();
    }
}
