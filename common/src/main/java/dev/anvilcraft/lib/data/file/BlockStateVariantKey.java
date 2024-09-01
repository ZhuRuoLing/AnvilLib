package dev.anvilcraft.lib.data.file;

import net.minecraft.world.level.block.state.properties.Property;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class BlockStateVariantKey {
    private final List<BlockStateVariantKeyPair<?, ?>> keyPairs = new ArrayList<>();

    public <T extends Property<V>, V extends Comparable<V>> BlockStateVariantKey add(
            T property,
            V value
    ) {
        keyPairs.add(BlockStateVariantKeyPair.of(property, value));
        return this;
    }

    public static BlockStateVariantKey build(Consumer<BlockStateVariantKey> builder){
        BlockStateVariantKey key = new BlockStateVariantKey();
        builder.accept(key);
        return key;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < keyPairs.size(); i++) {
            if (i != 0){
                sb.append(",");
            }
            sb.append(keyPairs.get(i).toString());
        }
        return sb.toString();
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
    }
}