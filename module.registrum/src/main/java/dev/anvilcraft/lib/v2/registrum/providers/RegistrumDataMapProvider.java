/*
 * Original work copyright (c) 2019 tterrag1098 (Registrate)
 * Modified work copyright (c) 2025 IThundxr (Registrate fork)
 * Additional modifications copyright (c) 2026 Anvil-Dev (AnvilLib-Registrum)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * Original File: https://github.com/IThundxr/Registrate/blob/1.21/dev/src/main/java/com/tterrag/registrate/providers/RegistrateDataMapProvider.java
 */

package dev.anvilcraft.lib.v2.registrum.providers;

import dev.anvilcraft.lib.v2.registrum.AbstractRegistrum;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.neoforged.fml.LogicalSide;
import net.neoforged.neoforge.common.data.DataMapProvider;

import java.util.concurrent.CompletableFuture;

public class RegistrumDataMapProvider extends DataMapProvider implements RegistrumProvider {

    private final AbstractRegistrum<?> parent;

    protected RegistrumDataMapProvider(AbstractRegistrum<?> parent, PackOutput output, CompletableFuture<HolderLookup.Provider> pvd) {
        super(output, pvd);
        this.parent = parent;
    }

    @Override
    public LogicalSide getSide() {
        return LogicalSide.SERVER;
    }

    @Override
    protected void gather(HolderLookup.Provider provider) {
        parent.genData(ProviderType.DATA_MAP, this);
    }
}
