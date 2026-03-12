package dev.anvilcraft.lib.v2.recipe.init;

import com.mojang.serialization.Codec;
import dev.anvilcraft.lib.v2.recipe.AnvilLibRecipe;
import dev.anvilcraft.lib.v2.recipe.data.advancement.predicate.item.AndPredicate;
import dev.anvilcraft.lib.v2.recipe.data.advancement.predicate.item.NotPredicate;
import dev.anvilcraft.lib.v2.recipe.data.advancement.predicate.item.OrPredicate;
import net.minecraft.advancements.critereon.ItemSubPredicate;
import net.minecraft.core.registries.BuiltInRegistries;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.jetbrains.annotations.NotNull;

public class LibItemSubPredicates {
    private static final DeferredRegister<ItemSubPredicate.Type<?>> DF = DeferredRegister.create(
        BuiltInRegistries.ITEM_SUB_PREDICATE_TYPE,
        AnvilLibRecipe.MAIN_ID
    );

    public static final DeferredHolder<ItemSubPredicate.Type<?>, ItemSubPredicate.Type<AndPredicate>> AND = register(
        "and",
        AndPredicate.CODEC
    );

    public static final DeferredHolder<ItemSubPredicate.Type<?>, ItemSubPredicate.Type<OrPredicate>> OR = register(
        "or",
        OrPredicate.CODEC
    );

    public static final DeferredHolder<ItemSubPredicate.Type<?>, ItemSubPredicate.Type<NotPredicate>> NOT = register(
        "not",
        NotPredicate.CODEC
    );

    public static <T extends ItemSubPredicate> @NotNull DeferredHolder<ItemSubPredicate.Type<?>, ItemSubPredicate.Type<T>> register(
        String name,
        Codec<T> codec
    ) {
        return DF.register(name, () -> new ItemSubPredicate.Type<>(codec));
    }

    public static void initialize(IEventBus modEventBus) {
        DF.register(modEventBus);
    }
}
