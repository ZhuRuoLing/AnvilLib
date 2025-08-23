package dev.anvilcraft.lib.recipe.predicate.function;

import com.mojang.serialization.Codec;
import dev.anvilcraft.lib.init.LibRegistries;
import dev.anvilcraft.lib.recipe.util.ISerializer;
import dev.anvilcraft.lib.recipe.util.InWorldRecipeContext;
import net.minecraft.resources.ResourceLocation;

import javax.annotation.Nullable;
import java.util.function.BiFunction;

public interface IPredicateFunction<T> extends BiFunction<InWorldRecipeContext, T, T> {
    Codec<IPredicateFunction<?>> CODEC = LibRegistries.PREDICATE_FUNCTION_TYPE_REGISTRY.byNameCodec()
        .dispatch(IPredicateFunction::getType, IPredicateFunction.Type::codec);

    IPredicateFunction.Type<?> getType();

    interface Type<O extends IPredicateFunction<?>> extends ISerializer<O> {
        default @Nullable ResourceLocation getId() {
            return LibRegistries.PREDICATE_FUNCTION_TYPE_REGISTRY.getKey(this);
        }
    }
}
