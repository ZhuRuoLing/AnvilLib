package dev.anvilcraft.lib.v2.recipe.event;

import dev.anvilcraft.lib.v2.recipe.InWorldRecipe;
import dev.anvilcraft.lib.v2.recipe.util.InWorldRecipeContext;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.crafting.RecipeType;
import net.neoforged.bus.api.Event;

@Getter
@ToString
@RequiredArgsConstructor
public class InWorldRecipeEvent extends Event {
    private final RecipeType<? extends InWorldRecipe> recipeType;
    private final Identifier id;
    private final InWorldRecipe recipe;
    private final InWorldRecipeContext context;
}
