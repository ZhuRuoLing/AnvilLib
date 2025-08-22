package dev.anvilcraft.lib.event.listener;

import dev.anvilcraft.lib.AnvilLib;
import dev.anvilcraft.lib.event.ItemEntityEvent;
import dev.anvilcraft.lib.init.reicpe.ModRecipeTriggers;
import dev.anvilcraft.lib.recipe.util.InWorldRecipeContext;
import dev.anvilcraft.lib.recipe.util.InWorldRecipeManager;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import org.jetbrains.annotations.NotNull;

@EventBusSubscriber(modid = AnvilLib.MOD_ID)
public class ItemEntityEventListener {
    @SubscribeEvent
    public static void onItemEntityInToBlock(@NotNull ItemEntityEvent.InToBlock event) {
        Level level = event.getLevel();
        if (!(level instanceof ServerLevel serverLevel)) return;
        InWorldRecipeManager manager = level.getRecipeManager().anvillib$getInWorldRecipeManager();
        InWorldRecipeContext context = new InWorldRecipeContext(serverLevel, event.getPos(), event.getEntity());
        manager.trigger(ModRecipeTriggers.ITEM_INTO_BLOCK, context);
        context.accept();
    }
}
