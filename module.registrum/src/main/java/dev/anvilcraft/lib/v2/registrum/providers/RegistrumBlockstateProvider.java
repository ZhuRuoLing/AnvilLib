/*
 * Original work copyright (c) 2019 tterrag1098 (Registrate)
 * Modified work copyright (c) 2025 IThundxr (Registrate fork)
 * Additional modifications copyright (c) 2026 Anvil-Dev (AnvilLib-Registrum)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * Original File: https://github.com/IThundxr/Registrate/blob/1.21/dev/src/main/java/com/tterrag/registrate/providers/RegistrateBlockstateProvider.java
 */

package dev.anvilcraft.lib.v2.registrum.providers;

import dev.anvilcraft.lib.v2.registrum.AbstractRegistrum;

import net.minecraft.data.PackOutput;
import net.minecraft.world.level.block.Block;
import net.neoforged.fml.LogicalSide;
import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
import net.neoforged.neoforge.client.model.generators.MultiPartBlockStateBuilder;
import net.neoforged.neoforge.client.model.generators.VariantBlockStateBuilder;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

import java.util.Optional;

public class RegistrumBlockstateProvider extends BlockStateProvider implements RegistrumProvider {

    private final AbstractRegistrum<?> parent;

    public RegistrumBlockstateProvider(AbstractRegistrum<?> parent, PackOutput packOutput, ExistingFileHelper exFileHelper) {
        super(packOutput, parent.getModid(), exFileHelper);
        this.parent = parent;
    }

    @Override
    public LogicalSide getSide() {
        return LogicalSide.CLIENT;
    }

    @Override
    protected void registerStatesAndModels() {
        parent.genData(ProviderType.BLOCKSTATE, this);
    }

    @Override
    public String getName() {
        return "Blockstates";
    }

    ExistingFileHelper getExistingFileHelper() {
        return this.models().existingFileHelper;
    }

    @SuppressWarnings("null")
    public Optional<VariantBlockStateBuilder> getExistingVariantBuilder(Block block) {
        return Optional.ofNullable(registeredBlocks.get(block))
            .filter(b -> b instanceof VariantBlockStateBuilder)
            .map(b -> (VariantBlockStateBuilder) b);
    }

    @SuppressWarnings("null")
    public Optional<MultiPartBlockStateBuilder> getExistingMultipartBuilder(Block block) {
        return Optional.ofNullable(registeredBlocks.get(block))
            .filter(b -> b instanceof MultiPartBlockStateBuilder)
            .map(b -> (MultiPartBlockStateBuilder) b);
    }
}
