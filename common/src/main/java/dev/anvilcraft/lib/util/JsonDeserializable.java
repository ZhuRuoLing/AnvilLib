package dev.anvilcraft.lib.util;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.serialization.JsonOps;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public interface JsonDeserializable {
    static @NotNull ItemStack toItemStack(@NotNull JsonElement element) {
        if (element.isJsonPrimitive()) {
            return new ItemStack(BuiltInRegistries.ITEM.get(new ResourceLocation(element.getAsString())));
        }
        JsonObject obj = element.getAsJsonObject();
        Item item = BuiltInRegistries.ITEM.get(new ResourceLocation(obj.get("id").getAsString()));
        ItemStack stack = new ItemStack(item);
        if (obj.has("count")) stack.setCount(obj.get("count").getAsInt());
        if (obj.has("data")) {
            CompoundTag.CODEC
                .parse(JsonOps.INSTANCE, obj.get("data"))
                .result()
                .ifPresent(nbt -> stack.setTag(stack.getOrCreateTag().merge(nbt)));
        }
        return stack;
    }
}
