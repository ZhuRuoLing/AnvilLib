package dev.anvilcraft.lib.recipe.injection;

import dev.anvilcraft.lib.recipe.InWorldRecipe;
import dev.anvilcraft.lib.recipe.util.InWorldRecipeManager;
import net.minecraft.core.HolderLookup;
import net.minecraft.world.item.crafting.RecipeHolder;

import java.util.List;

public interface IRecipeManagerExtension {
    default void anvillib$setInWorldRecipeManager(InWorldRecipeManager manager) {
        throw new AssertionError();
    }

    default InWorldRecipeManager anvillib$getInWorldRecipeManager() {
        throw new AssertionError();
    }

    default HolderLookup.Provider anvillib$getRegistries() {
        throw new AssertionError();
    }

    default void anvillib$addRecipes(List<RecipeHolder<InWorldRecipe>> recipes) {
        throw new AssertionError();
    }
}
