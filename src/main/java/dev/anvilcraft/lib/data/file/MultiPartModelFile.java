package dev.anvilcraft.lib.data.file;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import dev.anvilcraft.lib.data.JsonSerializable;
import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;
import java.util.List;

public class MultiPartModelFile extends ResourceFile {
    private final List<MultiPartModelElement> elements = new ArrayList<>();

    public MultiPartModelFile(ResourceLocation location) {
        super(location);
    }

    public MultiPartModelFile element(BlockStateVariant apply, BlockStateVariantKey when) {
        elements.add(new MultiPartModelElement(apply, when));
        return this;
    }

    @Override
    public JsonElement toJsonElement() {
        JsonObject root = new JsonObject();
        JsonArray multipart = new JsonArray();
        for (MultiPartModelElement element : elements) {
            multipart.add(element.toJsonElement());
        }
        root.add("multipart", multipart);
        return null;
    }

    public static class MultiPartModelElement implements JsonSerializable {
        private final BlockStateVariant apply;
        private final BlockStateVariantKey when;

        public MultiPartModelElement(BlockStateVariant apply, BlockStateVariantKey when) {
            this.apply = apply;
            this.when = when;
        }

        @Override
        public JsonElement toJsonElement() {
            JsonObject root = new JsonObject();
            root.add("apply", apply.toJsonElement());
            root.add("when", when.toJsonElement());
            return root;
        }
    }
}
