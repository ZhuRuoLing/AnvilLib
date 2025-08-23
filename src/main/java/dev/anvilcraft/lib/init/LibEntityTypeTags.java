package dev.anvilcraft.lib.init;

import dev.anvilcraft.lib.AnvilLib;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;

public class LibEntityTypeTags {
    public static final TagKey<EntityType<?>> ITEM_CACHE = bind("item_cache");

    @SuppressWarnings("unused")
    private static TagKey<EntityType<?>> bindC(String id) {
        return TagKey.create(Registries.ENTITY_TYPE, ResourceLocation.fromNamespaceAndPath("c", id));
    }

    @SuppressWarnings("SameParameterValue")
    private static TagKey<EntityType<?>> bind(String id) {
        return TagKey.create(Registries.ENTITY_TYPE, AnvilLib.of(id));
    }
}
