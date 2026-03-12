package dev.anvilcraft.lib.v2.recipe.mixin;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.MultimapBuilder;
import dev.anvilcraft.lib.v2.recipe.InWorldRecipe;
import dev.anvilcraft.lib.v2.recipe.injection.IRecipeMapExtension;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeMap;
import net.minecraft.world.item.crafting.RecipeType;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Mixin(RecipeMap.class)
public class RecipeMapMixin implements IRecipeMapExtension {
    @Mutable
    @Final
    @Shadow
    private Map<ResourceLocation, RecipeHolder<?>> byKey;

    @Mutable
    @Final
    @Shadow
    private Multimap<RecipeType<?>, RecipeHolder<?>> byType;

    public void anvillib$addRecipes(List<RecipeHolder<InWorldRecipe>> recipes) {
        ImmutableMap.Builder<ResourceLocation, RecipeHolder<?>> byNameBuilder = ImmutableMap.builder();
        Multimap<RecipeType<?>, RecipeHolder<?>> byTypeBuilder = MultimapBuilder.hashKeys().<RecipeHolder<?>>treeSetValues(
            Comparator.comparing(RecipeHolder::id)
        ).build();
        Set<ResourceLocation> keys = new HashSet<>();
        this.byKey.forEach((key, value) -> {
            if (key == null || value == null) return;
            if (keys.contains(key)) return;
            keys.add(key);
            byNameBuilder.put(key, value);
        });
        this.byType.forEach((key, value) -> {
            if (key == null || value == null) return;
            byTypeBuilder.put(key, value);
        });
        recipes.forEach(recipe -> {
            if (keys.contains(recipe.id().location())) return;
            keys.add(recipe.id().location());
            byNameBuilder.put(recipe.id().location(), recipe);
            byTypeBuilder.put(recipe.value().getType(), recipe);
        });
        this.byKey = byNameBuilder.build();
        this.byType = ImmutableMultimap.copyOf(byTypeBuilder);
    }
}
