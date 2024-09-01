package dev.anvilcraft.lib.data;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import net.minecraft.resources.ResourceLocation;

import java.util.HashMap;
import java.util.Map;

/**
 * simple implementation for block/item model file
 */
@SuppressWarnings("unchecked")
public class ModelFileBuilder<T extends ModelFileBuilder<T>> {
    final ResourceLocation location;
    final Map<String, String> textures = new HashMap<>();
    String parent;
    String renderType;

    public ModelFileBuilder(ResourceLocation location) {
        this.location = location;
    }

    public T texture(String key, String texture){
        textures.put(key, texture);
        return (T) this;
    }

    public T texture(String key, ResourceLocation texture){
        textures.put(key, texture.toString());
        return (T) this;
    }


    public T parent(String p){
        this.parent = p;
        return (T) this;
    }

    public T parent(ResourceLocation p){
        this.parent = p.toString();
        return (T) this;
    }

    public T renderType(String r){
        this.renderType = r;
        return (T) this;
    }

    public JsonElement finish(){
        JsonObject jsonObject = new JsonObject();
        if (!textures.isEmpty()){
            JsonObject jTextures = new JsonObject();
            textures.forEach((k,v) -> {
                jTextures.add(k, new JsonPrimitive(v));
            });
            jsonObject.add("textures", jTextures);
        }
        if (parent != null){
            jsonObject.add("parent", new JsonPrimitive(parent));
        }
        if (renderType != null){
            jsonObject.add("render_type", new JsonPrimitive(renderType));
        }
        return jsonObject;
    }
}
