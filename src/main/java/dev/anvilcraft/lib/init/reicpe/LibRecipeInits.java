package dev.anvilcraft.lib.init.reicpe;

import net.neoforged.bus.api.IEventBus;

public class LibRecipeInits {
    public static void init(IEventBus modEventBus) {
        LibRecipeTriggers.TRIGGER.register(modEventBus);
        LibRecipePredicateTypes.PREDICATE_TYPE.register(modEventBus);
        LibRecipeOutcomeTypes.OUTCOME_TYPE.register(modEventBus);
    }
}
