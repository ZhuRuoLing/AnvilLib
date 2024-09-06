package dev.anvilcraft.lib.data.provider;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Multimap;
import dev.anvilcraft.lib.mixin.LootContextParamSetsAccessor;
import dev.anvilcraft.lib.mixin.LootTableProviderAccessor;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSet;

import java.util.List;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class RegistratorLootTableProvider extends LootTableProvider {
    private final Multimap<LootContextParamSet, Consumer<BiConsumer<ResourceLocation, LootTable.Builder>>> lootActions = HashMultimap.create();

    public RegistratorLootTableProvider(PackOutput output) {
        super(output, Set.of(), List.of());
        ImmutableList.Builder<SubProviderEntry> entries = ImmutableList.builder();
        LootContextParamSetsAccessor.getREGISTRY().values().forEach(param -> entries.add(new SubProviderEntry(
            () -> cb -> lootActions.get(param).forEach(c -> c.accept(cb)),
            param
        )));
        ((LootTableProviderAccessor) this).setSubProviders(entries.build());
    }

    public void addLootAction(LootContextParamSet param, Consumer<BiConsumer<ResourceLocation, LootTable.Builder>> action) {
        lootActions.put(param, action);
    }
}
