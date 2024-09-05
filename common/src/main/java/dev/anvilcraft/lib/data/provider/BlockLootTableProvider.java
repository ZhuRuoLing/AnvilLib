package dev.anvilcraft.lib.data.provider;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.gson.JsonElement;
import dev.anvilcraft.lib.util.Callback;
import lombok.Setter;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.data.loot.LootTableSubProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.level.storage.loot.LootDataType;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSet;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import org.jetbrains.annotations.NotNull;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class BlockLootTableProvider extends BlockLootSubProvider implements LootTableSubProvider, DataProvider {
    private final String modid;
    private final PackOutput output;
    @Setter
    private boolean strictValidation = false;
    private final List<Callback<BlockLootTableProvider>> callbacks = new ArrayList<>();

    public BlockLootTableProvider(String modid, PackOutput output) {
        super(Collections.emptySet(), FeatureFlags.REGISTRY.allFlags());
        this.modid = modid;
        this.output = output;
    }

    public void addCallback(Callback<BlockLootTableProvider> callback) {
        callbacks.add(callback);
    }

    @Override
    public void generate() {
        callbacks.forEach(it -> it.invoke(this));
    }

    @Override
    public void generate(@NotNull BiConsumer<ResourceLocation, LootTable.Builder> biConsumer) {
        this.generate();
        for (Map.Entry<ResourceLocation, LootTable.Builder> entry : map.entrySet()) {
            ResourceLocation identifier = entry.getKey();

            if (identifier.equals(BuiltInLootTables.EMPTY)) {
                continue;
            }

            biConsumer.accept(identifier, entry.getValue());
        }

        if (strictValidation) {
            Set<ResourceLocation> missing = Sets.newHashSet();

            for (ResourceLocation blockId : BuiltInRegistries.BLOCK.keySet()) {
                if (blockId.getNamespace().equals(modid)) {
                    ResourceLocation blockLootTableId = BuiltInRegistries.BLOCK.get(blockId).getLootTable();
                    if (blockLootTableId.getNamespace().equals(modid)) {
                        if (!map.containsKey(blockLootTableId)) {
                            missing.add(blockId);
                        }
                    }
                }
            }

            if (!missing.isEmpty()) {
                throw new IllegalStateException("Missing loot table(s) for %s".formatted(missing));
            }
        }
    }

    @Override
    public CompletableFuture<?> run(CachedOutput output) {
        LootContextParamSet lootContextType = LootContextParamSets.BLOCK;
        HashMap<ResourceLocation, LootTable> tables = Maps.newHashMap();
        this.generate((location, builder) -> tables.put(location, builder.build()));
        List<CompletableFuture<?>> futures = new ArrayList<>();
        tables.forEach((location, lootTable) -> {
            JsonElement jsonElement = LootDataType.TABLE.parser().toJsonTree(lootTable);
            futures.add(DataProvider.saveStable(output, jsonElement,
                this.output.createPathProvider(PackOutput.Target.DATA_PACK, "loot_tables")
                    .json(location)
            ));
        });
        return CompletableFuture.allOf(futures.toArray(CompletableFuture[]::new));
    }

    @Override
    public String getName() {
        return modid + "->BlockLootTables";
    }
}
