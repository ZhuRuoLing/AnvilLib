package dev.anvilcraft.lib.data.file;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import dev.anvilcraft.lib.data.JsonSerializable;
import net.minecraft.world.level.block.state.properties.Property;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class BlockStateVariantKey implements JsonSerializable {
    private final List<BlockStateVariantKeyPair<?, ?>> keyPairs = new ArrayList<>();

    public <T extends Property<V>, V extends Comparable<V>> BlockStateVariantKey then(
            T property,
            V value
    ) {
        keyPairs.add(BlockStateVariantKeyPair.of(property, value));
        return this;
    }

    public static <T extends Property<V>, V extends Comparable<V>> BlockStateVariantKey with(
            T property,
            V value
    ) {
        return new BlockStateVariantKey().then(property, value);
    }

    public static BlockStateVariantKey build(Consumer<BlockStateVariantKey> builder) {
        BlockStateVariantKey key = new BlockStateVariantKey();
        builder.accept(key);
        return key;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < keyPairs.size(); i++) {
            if (i != 0) {
                sb.append(",");
            }
            sb.append(keyPairs.get(i).toString());
        }
        return sb.toString();
    }

    @Override
    public JsonElement toJsonElement() {
        JsonObject root = new JsonObject();
        keyPairs.forEach(kp -> kp.encodeIntoJsonObject(root));
        return root;
    }

    public record BlockStateVariantKeyPair<T extends Property<V>, V extends Comparable<V>>(T property, V value) {

        @Override
        public String toString() {
            return property.getName() + "=" + property.getName(value);
        }

        public static <T extends Property<V>, V extends Comparable<V>> BlockStateVariantKeyPair<T, V> of(
                T property,
                V value
        ) {
            return new BlockStateVariantKeyPair<>(property, value);
        }

        public void encodeIntoJsonObject(JsonObject root) {
            root.add(property.getName(), new JsonPrimitive(property.getName(value)));
        }
    }
}