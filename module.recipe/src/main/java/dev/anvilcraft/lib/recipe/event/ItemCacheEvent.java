package dev.anvilcraft.lib.recipe.event;

import dev.anvilcraft.lib.recipe.cache.ItemCache;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import net.minecraft.world.entity.item.ItemEntity;
import net.neoforged.bus.api.Event;

@Getter
@ToString
@RequiredArgsConstructor
public class ItemCacheEvent extends Event {
    protected final ItemCache cache;

    @Getter
    @ToString
    public static class SpawnItemEntity extends ItemCacheEvent {
        private final ItemEntity entity;

        public SpawnItemEntity(ItemCache cache, ItemEntity entity) {
            super(cache);
            this.entity = entity;
        }
    }
}
