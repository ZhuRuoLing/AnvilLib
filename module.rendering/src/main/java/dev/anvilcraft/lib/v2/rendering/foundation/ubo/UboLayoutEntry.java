package dev.anvilcraft.lib.v2.rendering.foundation.ubo;

import com.mojang.blaze3d.buffers.Std140Builder;
import com.mojang.blaze3d.buffers.Std140SizeCalculator;
import lombok.Getter;

import java.util.function.Function;
import java.util.function.Supplier;

@Getter
public class UboLayoutEntry<T, I> {
    private final UboLayoutEntryType<T> type;
    private final Function<I, T> getter;

    public UboLayoutEntry(UboLayoutEntryType<T> type, Function<I, T> getter) {
        this.type = type;
        this.getter = getter;
    }

    void acceptSizeCalculator(Std140SizeCalculator sizeCalculator) {
        type.acceptSizeCalculator(sizeCalculator);
    }

    void acceptWriter(Std140Builder writer, I object) {
        type.acceptWriter(writer, getter.apply(object));
    }

    public static <T1, I1> Builder<T1, I1> builder(UboLayoutEntryType<T1> type) {
        return new Builder<>(type);
    }

    public static <I1> Builder<Integer, I1> ofInt() {
        return builder(UboLayoutEntryType.INT);
    }

    public static <I1> Builder<Float, I1> ofFloat() {
        return builder(UboLayoutEntryType.FLOAT);
    }

    public static <I1> Builder<org.joml.Vector2f, I1> ofVec2() {
        return builder(UboLayoutEntryType.VEC2);
    }

    public static <I1> Builder<org.joml.Vector3f, I1> ofVec3() {
        return builder(UboLayoutEntryType.VEC3);
    }

    public static <I1> Builder<org.joml.Vector4f, I1> ofVec4() {
        return builder(UboLayoutEntryType.VEC4);
    }

    public static <I1> Builder<org.joml.Matrix4f, I1> ofMat4() {
        return builder(UboLayoutEntryType.MAT4);
    }

    public static class Builder<T, I> {
        private final UboLayoutEntryType<T> type;
        private Function<I, T> getter;

        public Builder(UboLayoutEntryType<T> type) {
            this.type = type;
        }

        public Builder<T, I> forGetter(Function<I, T> getter) {
            this.getter = getter;
            return this;
        }

        public UboLayoutEntry<T, I> build() {
            return new UboLayoutEntry<>(type, getter);
        }
    }
}
