package dev.anvilcraft.lib.init;

import dev.anvilcraft.lib.AnvilLib;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.entity.BlockEntityType;

public class LibBlockEntityTags {
    public static final TagKey<BlockEntityType<?>> ITEM_CACHE = TagKey.create(Registries.BLOCK_ENTITY_TYPE, AnvilLib.of("item_cache"));
}
