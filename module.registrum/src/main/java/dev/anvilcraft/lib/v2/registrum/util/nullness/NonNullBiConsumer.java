/*
 *
 *  * Original work copyright (c) 2019 tterrag1098 (Registrate)
 *  * Additional modifications copyright (c) 2026 Anvil-Dev (AnvilLib-Registrum)
 *  *
 *  * This Source Code Form is subject to the terms of the Mozilla Public
 *  * License, v. 2.0. If a copy of the MPL was not distributed with this
 *  * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *  *
 *  * Original File: https://github.com/tterrag1098/Registrate/blob/1.21.5/dev/D:/Projects/repos/AnvilLib/module.registrum/src/main/java/dev/anvilcraft/lib/v2/registrum/util/nullness/NonNullBiConsumer.java
 *
 */

package dev.anvilcraft.lib.v2.registrum.util.nullness;

import java.util.function.BiConsumer;

@FunctionalInterface
public interface NonNullBiConsumer<@NonnullType T, @NonnullType U> extends BiConsumer<T, U> {

    @Override
    void accept(T t, U u);

    static <T, U> NonNullBiConsumer<T, U> noop() {
        return (t, u) -> {
        };
    }
}
