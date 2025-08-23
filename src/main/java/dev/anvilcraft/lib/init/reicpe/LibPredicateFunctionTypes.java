package dev.anvilcraft.lib.init.reicpe;

import dev.anvilcraft.lib.AnvilLib;
import dev.anvilcraft.lib.init.LibRegistries;
import dev.anvilcraft.lib.recipe.predicate.function.IPredicateFunction;
import dev.anvilcraft.lib.recipe.predicate.function.SaveComponentToTag;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class LibPredicateFunctionTypes {
    public static final DeferredRegister<IPredicateFunction.Type<?>> PREDICATE_FUNCTION_TYPE = DeferredRegister
        .create(LibRegistries.PREDICATE_FUNCTION_TYPE_REGISTRY, AnvilLib.MOD_ID);

    public static final DeferredHolder<IPredicateFunction.Type<?>, SaveComponentToTag.Type> SAVE_COMPONENT_TO_TAG = PREDICATE_FUNCTION_TYPE
        .register("save_component_2_tag", SaveComponentToTag.Type::new);
}
