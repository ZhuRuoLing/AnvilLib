package dev.anvilcraft.lib.mixin;

import net.minecraft.data.loot.LootTableProvider;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.List;

@Mixin(LootTableProvider.class)
public interface LootTableProviderAccessor {
    @Mutable
    @Accessor
    void setSubProviders(List<LootTableProvider.SubProviderEntry> entries);
}