package dev.anvilcraft.lib.mixin;

import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(Item.Properties.class)
public interface ItemPropertiesAccessor {
    @Accessor
    int getMaxStackSize();

    @Accessor
    int getMaxDamage();

    @Accessor
    @Nullable
    Item getCraftingRemainingItem();

    @Accessor
    Rarity getRarity();

    @Accessor
    @Nullable
    FoodProperties getFoodProperties();

    @Accessor("isFireResistant")
    boolean isFireResistant();
}
