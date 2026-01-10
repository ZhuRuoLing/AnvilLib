package dev.anvilcraft.lib.recipe.init.reicpe;

import dev.anvilcraft.lib.recipe.AnvilLibRecipe;
import dev.anvilcraft.lib.recipe.init.LibRegistries;
import dev.anvilcraft.lib.recipe.trigger.IRecipeTrigger;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class LibRecipeTriggers {
    public static final DeferredRegister<IRecipeTrigger> TRIGGER = DeferredRegister
        .create(LibRegistries.TRIGGER_REGISTRY, AnvilLibRecipe.MOD_ID);

    public static final DeferredHolder<IRecipeTrigger, IRecipeTrigger> ITEM_INTO_BLOCK = TRIGGER.register(
        "item_into_block",
        IRecipeTrigger.Impl::new
    );
}
