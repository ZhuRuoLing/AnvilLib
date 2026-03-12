/*
 * Original work copyright (c) 2019 tterrag1098 (Registrate)
 * Modified work copyright (c) 2025 IThundxr (Registrate fork)
 * Additional modifications copyright (c) 2026 Anvil-Dev (AnvilLib-Registrum)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * Original File: https://github.com/IThundxr/Registrate/blob/1.21/dev/src/main/java/com/tterrag/registrate/providers/loot/RegistrateEntityLootTables.java
 */

package dev.anvilcraft.lib.v2.registrum.providers.loot;

import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Stream;

import dev.anvilcraft.lib.v2.registrum.AbstractRegistrum;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.loot.EntityLootSubProvider;
import net.minecraft.data.loot.packs.VanillaEntityLoot;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.FrogVariant;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;

public class RegistrumEntityLootTables extends VanillaEntityLoot implements RegistrumLootTables {

    private final AbstractRegistrum<?> parent;
    private final Consumer<RegistrumEntityLootTables> callback;

    public RegistrumEntityLootTables(HolderLookup.Provider p_346214_, AbstractRegistrum<?> parent, Consumer<RegistrumEntityLootTables> callback) {
        super(p_346214_);
        this.parent = parent;
        this.callback = callback;
    }

    @Override
    public void generate() {
        callback.accept(this);
    }

    @Override
    protected Stream<EntityType<?>> getKnownEntityTypes() {
        return parent.getAll(Registries.ENTITY_TYPE).stream().map(Supplier::get);
    }

    public HolderLookup.Provider getRegistries() {
        return this.registries;
    }

    public static LootTable.Builder createSheepTable(ItemLike p_249422_) { return EntityLootSubProvider.createSheepTable(p_249422_); }

    @Override
    public boolean canHaveLootTable(EntityType<?> p_249029_) { return super.canHaveLootTable(p_249029_); }

    @Override
    public LootItemCondition.Builder killedByFrogVariant(ResourceKey<FrogVariant> p_335676_) { return super.killedByFrogVariant(p_335676_); }

    @Override
    public void add(EntityType<?> p_248740_, LootTable.Builder p_249440_) { super.add(p_248740_, p_249440_); }

    @Override
    public void add(EntityType<?> p_252130_, ResourceKey<LootTable> p_335943_, LootTable.Builder p_249357_) { super.add(p_252130_, p_335943_, p_249357_); }
}
