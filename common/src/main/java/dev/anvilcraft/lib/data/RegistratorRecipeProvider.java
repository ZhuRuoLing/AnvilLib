package dev.anvilcraft.lib.data;

import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeProvider;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

public class RegistratorRecipeProvider extends RecipeProvider implements Consumer<FinishedRecipe> {
    private final List<FinishedRecipe> recipes = Collections.synchronizedList(new ArrayList<>());
    private Consumer<FinishedRecipe> writer;

    public RegistratorRecipeProvider(PackOutput output) {
        super(output);
    }

    @Override
    public void accept(FinishedRecipe finishedRecipe) {
        if (this.writer == null) {
            this.recipes.add(finishedRecipe);
            return;
        }
        this.writer.accept(finishedRecipe);
    }

    @Override
    public void buildRecipes(@NotNull Consumer<FinishedRecipe> writer) {
        this.writer = writer;
        this.recipes.forEach(writer);
    }
}
