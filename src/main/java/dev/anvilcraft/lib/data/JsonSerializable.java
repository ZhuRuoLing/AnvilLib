package dev.anvilcraft.lib.data;

import com.google.gson.JsonElement;

public interface JsonSerializable {
    JsonElement toJsonElement();
}
