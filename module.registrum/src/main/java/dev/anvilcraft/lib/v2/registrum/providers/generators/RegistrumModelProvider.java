/*
 *
 *  * Original work copyright (c) 2019 tterrag1098 (Registrate)
 *  * Additional modifications copyright (c) 2026 Anvil-Dev (AnvilLib-Registrum)
 *  *
 *  * This Source Code Form is subject to the terms of the Mozilla Public
 *  * License, v. 2.0. If a copy of the MPL was not distributed with this
 *  * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *  *
 *  * Original File: https://github.com/tterrag1098/Registrate/blob/1.21.5/dev/D:/Projects/repos/AnvilLib/module.registrum/src/main/java/dev/anvilcraft/lib/v2/registrum/providers/generators/RegistrumModelProvider.java
 *
 */

package dev.anvilcraft.lib.v2.registrum.providers.generators;

import dev.anvilcraft.lib.v2.registrum.AbstractRegistrum;
import dev.anvilcraft.lib.v2.registrum.providers.RegistrumProvider;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.ModelProvider;
import net.minecraft.data.PackOutput;
import net.neoforged.fml.LogicalSide;

public class RegistrumModelProvider extends ModelProvider implements RegistrumProvider {

    private final AbstractRegistrum<?> parent;

    public RegistrumModelProvider(AbstractRegistrum<?> parent, PackOutput p_388260_) {
        super(p_388260_, parent.getModid());
        this.parent = parent;
    }

    @Override
    protected void registerModels(BlockModelGenerators blockModels, ItemModelGenerators itemModels) {
        new RegistrumBlockModelGenerator(parent, blockModels.blockStateOutput, blockModels.itemModelOutput, blockModels.modelOutput).run();
        new RegistrumItemModelGenerator(parent, itemModels.itemModelOutput, itemModels.modelOutput).run();
    }

    @Override
    public LogicalSide getSide() {
        return LogicalSide.CLIENT;
    }

}
