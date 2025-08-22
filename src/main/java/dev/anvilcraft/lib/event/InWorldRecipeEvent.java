package dev.anvilcraft.lib.event;

import dev.anvilcraft.lib.recipe.InWorldRecipe;
import dev.anvilcraft.lib.recipe.util.InWorldRecipeContext;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeType;
import net.neoforged.bus.api.Event;

@Getter
@ToString
@RequiredArgsConstructor
public class InWorldRecipeEvent extends Event {
    private final RecipeType<? extends InWorldRecipe> recipeType;
    private final ResourceLocation id;
    private final InWorldRecipe recipe;
    private final InWorldRecipeContext context;
}
