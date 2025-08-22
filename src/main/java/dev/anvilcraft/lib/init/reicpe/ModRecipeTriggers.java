package dev.anvilcraft.lib.init.reicpe;

import dev.anvilcraft.lib.AnvilLib;
import dev.anvilcraft.lib.init.ModRegistries;
import dev.anvilcraft.lib.recipe.trigger.IRecipeTrigger;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModRecipeTriggers {
    public static final DeferredRegister<IRecipeTrigger> TRIGGER = DeferredRegister
        .create(ModRegistries.TRIGGER_REGISTRY, AnvilLib.MOD_ID);

    public static final DeferredHolder<IRecipeTrigger, IRecipeTrigger> ITEM_INTO_BLOCK = TRIGGER.register(
        "item_into_block",
        IRecipeTrigger.Impl::new
    );
}
