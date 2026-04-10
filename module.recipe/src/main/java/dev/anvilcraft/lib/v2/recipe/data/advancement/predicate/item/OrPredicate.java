package dev.anvilcraft.lib.v2.recipe.data.advancement.predicate.item;

import com.google.common.collect.ImmutableList;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.anvilcraft.lib.v2.codec.CodecUtil;
import net.minecraft.core.component.DataComponentGetter;
import net.minecraft.core.component.predicates.DataComponentPredicate;

import java.util.List;
import java.util.Map;

public record OrPredicate(List<Map.Entry<Type<?>, DataComponentPredicate>> subPredicates) implements DataComponentPredicate {
    public static final Codec<OrPredicate> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        CodecUtil.<Type<?>, DataComponentPredicate, Map.Entry<Type<?>, DataComponentPredicate>>byMap(
                DataComponentPredicate.CODEC,
                Map.Entry::getKey,
                Map.Entry::getValue,
                Map::entry
            )
            .listOf()
            .optionalFieldOf("predicates", List.of())
            .forGetter(OrPredicate::subPredicates)
    ).apply(instance, OrPredicate::new));

    @Override
    public boolean matches(DataComponentGetter getter) {
        return this.subPredicates().stream().anyMatch(it -> it.getValue().matches(getter));
    }

    public static OrPredicate of(Type<?> type, DataComponentPredicate subPredicate) {
        return new OrPredicate(List.of(Map.entry(type, subPredicate)));
    }

    public OrPredicate with(Type<?> type, DataComponentPredicate subPredicate) {
        ImmutableList.Builder<Map.Entry<Type<?>, DataComponentPredicate>> builder = ImmutableList.builder();
        builder.addAll(this.subPredicates());
        builder.add(Map.entry(type, subPredicate));
        return new OrPredicate(builder.build());
    }
}
