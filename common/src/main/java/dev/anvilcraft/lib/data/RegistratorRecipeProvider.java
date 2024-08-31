package dev.anvilcraft.lib.data;

import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeProvider;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

public class RegistratorRecipeProvider extends RecipeProvider implements Consumer<FinishedRecipe> {
    public RegistratorRecipeProvider(PackOutput output) {
        super(output);
    }

    @Override
    public void accept(FinishedRecipe finishedRecipe) {

    }

    @Override
    public void buildRecipes(@NotNull Consumer<FinishedRecipe> writer) {

    }
}
