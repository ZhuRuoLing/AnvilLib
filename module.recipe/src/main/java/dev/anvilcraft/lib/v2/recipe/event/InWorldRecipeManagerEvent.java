package dev.anvilcraft.lib.v2.recipe.event;

import dev.anvilcraft.lib.v2.recipe.util.InWorldRecipeManager;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import net.minecraft.world.item.crafting.RecipeManager;
import net.neoforged.bus.api.Event;

@Getter
@ToString
@RequiredArgsConstructor
public class InWorldRecipeManagerEvent extends Event {
    private final InWorldRecipeManager manager;

    @Getter
    @ToString
    public static class Init extends InWorldRecipeManagerEvent {
        private final RecipeManager recipeManager;

        public Init(InWorldRecipeManager manager, RecipeManager recipeManager) {
            super(manager);
            this.recipeManager = recipeManager;
        }
    }
}
