package dev.anvilcraft.lib.data.file;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import dev.anvilcraft.lib.data.JsonSerializable;
import lombok.Getter;
import net.minecraft.resources.ResourceLocation;

import java.util.function.Consumer;

@Getter
public class BlockStateVariant implements JsonSerializable {

    private String model;
    private int rotationX = 0;
    private int rotationY = 0;
    private boolean uvLock = false;

    public BlockStateVariant model(String model) {
        this.model = model;
        return this;
    }

    public BlockStateVariant model(ResourceLocation model) {
        return model(model.toString());
    }

    public BlockStateVariant rotationX(int rotation) {
        rotationX = rotation;
        return this;
    }

    public BlockStateVariant uvLock(boolean l){
        this.uvLock = l;
        return this;
    }

    public BlockStateVariant rotationY(int rotation) {
        rotationY = rotation;
        return this;
    }

    public static BlockStateVariant build(Consumer<BlockStateVariant> builder){
        BlockStateVariant key = new BlockStateVariant();
        builder.accept(key);
        return key;
    }

    public static BlockStateVariant fromModel(ResourceLocation resourceLocation){
        String ns = resourceLocation.getNamespace();
        String path = resourceLocation.getPath();
        if (path.startsWith("models/")){
            path = path.replaceFirst("models/", "");
        }
        return new BlockStateVariant().model(new ResourceLocation(ns, path));
    }

    @Override
    public JsonElement toJsonElement() {
        JsonObject root = new JsonObject();
        root.add("model", new JsonPrimitive(model));
        root.add("x", new JsonPrimitive(rotationX));
        root.add("y", new JsonPrimitive(rotationY));
        root.add("uvlock", new JsonPrimitive(uvLock));
        return root;
    }
}