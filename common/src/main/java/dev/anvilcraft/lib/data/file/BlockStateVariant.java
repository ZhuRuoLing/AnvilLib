package dev.anvilcraft.lib.data.file;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import lombok.Getter;
import net.minecraft.resources.ResourceLocation;

import java.util.function.Consumer;

@Getter
public class BlockStateVariant implements ResourceFile {

    private String model;
    private int rotationX = 0;
    private int rotationY = 0;
    private int rotationZ = 0;

    public BlockStateVariant model(String model) {
        this.model = model;
        return this;
    }

    public BlockStateVariant model(ResourceLocation model) {
        return model(model.toString());
    }

    public BlockStateVariant rotX(int rotation) {
        rotationX = rotation;
        return this;
    }

    public BlockStateVariant rotY(int rotation) {
        rotationY = rotation;
        return this;
    }

    public BlockStateVariant rotZ(int rotation) {
        rotationZ = rotation;
        return this;
    }

    public static BlockStateVariant build(Consumer<BlockStateVariant> builder){
        BlockStateVariant key = new BlockStateVariant();
        builder.accept(key);
        return key;
    }

    @Override
    public JsonElement toJsonElement() {
        JsonObject root = new JsonObject();
        root.add("model", new JsonPrimitive(model));
        root.add("x", new JsonPrimitive(rotationX));
        root.add("y", new JsonPrimitive(rotationY));
        root.add("z", new JsonPrimitive(rotationZ));
        return root;
    }
}