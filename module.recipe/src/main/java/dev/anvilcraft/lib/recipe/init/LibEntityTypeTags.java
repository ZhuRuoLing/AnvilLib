package dev.anvilcraft.lib.recipe.init;

import dev.anvilcraft.lib.recipe.AnvilLibRecipe;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;

public class LibEntityTypeTags {
    public static final TagKey<EntityType<?>> ITEM_CACHE = bind("item_cache");

    @SuppressWarnings("SameParameterValue")
    private static TagKey<EntityType<?>> bind(String id) {
        return TagKey.create(Registries.ENTITY_TYPE, AnvilLibRecipe.of(id));
    }
}
