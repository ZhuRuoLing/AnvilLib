package dev.anvilcraft.lib.init;

import dev.anvilcraft.lib.AnvilLib;
import dev.anvilcraft.lib.recipe.outcome.IRecipeOutcome;
import dev.anvilcraft.lib.recipe.predicate.IRecipePredicate;
import dev.anvilcraft.lib.recipe.trigger.IRecipeTrigger;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.registries.NewRegistryEvent;
import net.neoforged.neoforge.registries.RegistryBuilder;

@EventBusSubscriber(modid = AnvilLib.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public class ModRegistries {
    public static final ResourceKey<Registry<IRecipeTrigger>> TRIGGER_KEY = ResourceKey.createRegistryKey(
        AnvilLib.of("trigger")
    );
    public static final Registry<IRecipeTrigger> TRIGGER_REGISTRY = new RegistryBuilder<>(TRIGGER_KEY)
        .sync(true)
        .maxId(512)
        .create();

    public static final ResourceKey<Registry<IRecipeOutcome.Type<?>>> OUTCOME_KEY = ResourceKey.createRegistryKey(
        AnvilLib.of("outcome")
    );
    public static final Registry<IRecipeOutcome.Type<?>> OUTCOME_TYPE_REGISTRY = new RegistryBuilder<>(OUTCOME_KEY)
        .sync(true)
        .maxId(512)
        .create();

    public static final ResourceKey<Registry<IRecipePredicate.Type<?>>> PREDICATE_KEY = ResourceKey.createRegistryKey(
        AnvilLib.of("predicate")
    );
    public static final Registry<IRecipePredicate.Type<?>> PREDICATE_TYPE_REGISTRY = new RegistryBuilder<>(PREDICATE_KEY)
        .sync(true)
        .maxId(512)
        .create();

    @SubscribeEvent
    public static void registerRegistries(NewRegistryEvent event) {
        event.register(TRIGGER_REGISTRY);
        event.register(OUTCOME_TYPE_REGISTRY);
        event.register(PREDICATE_TYPE_REGISTRY);
    }
}
