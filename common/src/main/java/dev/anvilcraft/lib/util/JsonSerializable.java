package dev.anvilcraft.lib.util;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public interface JsonSerializable {
    JsonElement toJson();

    static @NotNull JsonArray fromList(@NotNull Iterable<? extends JsonSerializable> iterable) {
        JsonArray array = new JsonArray();
        iterable.forEach(element -> array.add(element.toJson()));
        return array;
    }

    static @NotNull JsonElement fromItemStack(@NotNull ItemStack stack) {
        Item item = stack.getItem();
        ItemStack instance = item.getDefaultInstance();
        CompoundTag nbt = stack.getTag();
        int count = stack.getCount();
        String id = BuiltInRegistries.ITEM.getKey(item).toString();
        if (count == 1 && (nbt == null || ItemStack.isSameItemSameTags(instance, stack))) {
            return new JsonPrimitive(id);
        }
        JsonObject obj = new JsonObject();
        obj.addProperty("id", id);
        obj.addProperty("count", count);
        if (nbt != null) {
            CompoundTag instanceNbt = instance.getTag();
            if (instanceNbt == null) {
                obj.addProperty("data", nbt.toString());
            } else {
                CompoundTag diff = nbt.copy();
                for (String key : instanceNbt.getAllKeys()) {
                    if (Objects.equals(diff.get(key), instanceNbt.get(key))) diff.remove(key);
                }
                if (!diff.isEmpty()) obj.addProperty("data", diff.toString());
            }
        }
        return obj;
    }
}
