package dev.anvilcraft.lib.v2.recipe.data.advancement.predicate.item;

import com.mojang.serialization.Codec;
import dev.anvilcraft.lib.v2.codec.CodecUtil;
import net.minecraft.core.component.DataComponentGetter;
import net.minecraft.core.component.predicates.DataComponentPredicate;

public record NotPredicate(Type<?> type, DataComponentPredicate subPredicate) implements DataComponentPredicate {
    public static final Codec<NotPredicate> CODEC = CodecUtil.byMap(
        DataComponentPredicate.CODEC,
        NotPredicate::type,
        NotPredicate::subPredicate,
        NotPredicate::new
    );

    @Override
    public boolean matches(DataComponentGetter getter) {
        return !this.subPredicate.matches(getter);
    }

    public static NotPredicate of(Type<?> type, DataComponentPredicate subPredicate) {
        return new NotPredicate(type, subPredicate);
    }
}
