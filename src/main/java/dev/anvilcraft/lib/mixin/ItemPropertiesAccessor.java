package dev.anvilcraft.lib.mixin;

import net.minecraft.world.item.Item;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(Item.Properties.class)
public interface ItemPropertiesAccessor {
    @Accessor
    @Nullable
    Item getCraftingRemainingItem();
}
