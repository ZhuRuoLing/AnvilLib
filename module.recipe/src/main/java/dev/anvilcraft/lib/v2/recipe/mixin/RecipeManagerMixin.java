package dev.anvilcraft.lib.v2.recipe.mixin;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.MultimapBuilder;
import dev.anvilcraft.lib.v2.recipe.injection.IRecipeManagerExtension;
import dev.anvilcraft.lib.v2.recipe.InWorldRecipe;
import dev.anvilcraft.lib.v2.recipe.util.InWorldRecipeManager;
import net.minecraft.core.HolderLookup;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.RecipeType;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Mixin(RecipeManager.class)
abstract class RecipeManagerMixin implements IRecipeManagerExtension {
    @Shadow
    @Final
    private HolderLookup.Provider registries;

    @Shadow
    private Map<ResourceLocation, RecipeHolder<?>> byName;

    @Shadow
    private Multimap<RecipeType<?>, RecipeHolder<?>> byType;
    @Unique
    private InWorldRecipeManager anvillib$inWorldRecipeManager = null;

    @Override
    public void anvillib$setInWorldRecipeManager(InWorldRecipeManager manager) {
        this.anvillib$inWorldRecipeManager = manager;
    }

    @Override
    public InWorldRecipeManager anvillib$getInWorldRecipeManager() {
        return this.anvillib$inWorldRecipeManager;
    }

    @Override
    public HolderLookup.Provider anvillib$getRegistries() {
        return this.registries;
    }

    @Override
    public void anvillib$addRecipes(@NotNull List<RecipeHolder<InWorldRecipe>> recipes) {
        ImmutableMap.Builder<ResourceLocation, RecipeHolder<?>> byNameBuilder = ImmutableMap.builder();
        Multimap<RecipeType<?>, RecipeHolder<?>> byTypeBuilder = MultimapBuilder.hashKeys().<RecipeHolder<?>>treeSetValues(
            Comparator.comparing(RecipeHolder::id)
        ).build();
        Set<ResourceLocation> keys = new HashSet<>();
        this.byName.forEach((key, value) -> {
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
            if (keys.contains(recipe.id())) return;
            keys.add(recipe.id());
            byNameBuilder.put(recipe.id(), recipe);
            byTypeBuilder.put(recipe.value().getType(), recipe);
        });
        this.byName = byNameBuilder.build();
        this.byType = ImmutableMultimap.copyOf(byTypeBuilder);
    }
}
