package dev.anvilcraft.lib.data.file;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.resources.ResourceLocation;

import java.util.HashMap;
import java.util.Map;

public class BlockStateFile extends ResourceFile {

    private final Map<BlockStateVariantKey, BlockStateVariant> variants = new HashMap<>();

    public BlockStateFile(ResourceLocation location) {
        super(location);
    }

    public BlockStateFile single(BlockStateVariant v) {
        variants.put(new BlockStateVariantKey(), v);
        return this;
    }

    public BlockStateFile variant(
            BlockStateVariantKey key,
            BlockStateVariant variant
    ) {
        this.variants.put(key, variant);
        return this;
    }

    @Override
    public JsonElement toJsonElement() {
        JsonObject root = new JsonObject();
        JsonObject variants = new JsonObject();
        this.variants.forEach((key, var) -> variants.add(key.toString(), var.toJsonElement()));
        root.add("variants", variants);
        return root;
    }

}
