package dev.anvilcraft.lib.v2.recipe.data.advancement.predicate.item;

import com.google.common.collect.ImmutableList;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.anvilcraft.lib.v2.recipe.util.CodecUtil;
import net.minecraft.core.component.DataComponentGetter;
import net.minecraft.core.component.predicates.DataComponentPredicate;

import java.util.List;
import java.util.Map;

public record AndPredicate(List<Map.Entry<Type<?>, DataComponentPredicate>> subPredicates) implements DataComponentPredicate {
    public static final Codec<AndPredicate> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        CodecUtil.<Type<?>, DataComponentPredicate, Map.Entry<Type<?>, DataComponentPredicate>>byMap(
                DataComponentPredicate.CODEC,
                Map.Entry::getKey,
                Map.Entry::getValue,
                Map::entry
            )
            .listOf()
            .optionalFieldOf("predicates", List.of())
            .forGetter(AndPredicate::subPredicates)
    ).apply(instance, AndPredicate::new));

    @Override
    public boolean matches(DataComponentGetter getter) {
        return this.subPredicates().stream().allMatch(it -> it.getValue().matches(getter));
    }

    public static AndPredicate of(Type<?> type, DataComponentPredicate subPredicate) {
        return new AndPredicate(List.of(Map.entry(type, subPredicate)));
    }

    public AndPredicate with(Type<?> type, DataComponentPredicate subPredicate) {
        ImmutableList.Builder<Map.Entry<Type<?>, DataComponentPredicate>> builder = ImmutableList.builder();
        builder.addAll(this.subPredicates());
        builder.add(Map.entry(type, subPredicate));
        return new AndPredicate(builder.build());
    }
}
