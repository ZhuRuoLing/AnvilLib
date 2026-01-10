package dev.anvilcraft.lib.recipe.init;

import dev.anvilcraft.lib.recipe.AnvilLibRecipe;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.entity.BlockEntityType;

public class LibBlockEntityTags {
    public static final TagKey<BlockEntityType<?>> ITEM_CACHE = TagKey.create(Registries.BLOCK_ENTITY_TYPE, AnvilLibRecipe.of("item_cache"));
}
