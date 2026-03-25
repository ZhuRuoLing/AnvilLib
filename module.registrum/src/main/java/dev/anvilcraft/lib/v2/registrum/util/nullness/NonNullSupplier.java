/*
 *
 *  * Original work copyright (c) 2019 tterrag1098 (Registrate)
 *  * Additional modifications copyright (c) 2026 Anvil-Dev (AnvilLib-Registrum)
 *  *
 *  * This Source Code Form is subject to the terms of the Mozilla Public
 *  * License, v. 2.0. If a copy of the MPL was not distributed with this
 *  * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *  *
 *  * Original File: https://github.com/tterrag1098/Registrate/blob/1.21.5/dev/D:/Projects/repos/AnvilLib/module.registrum/src/main/java/dev/anvilcraft/lib/v2/registrum/util/nullness/NonNullSupplier.java
 *
 */

package dev.anvilcraft.lib.v2.registrum.util.nullness;

import net.neoforged.neoforge.common.util.Lazy;

import java.util.Objects;
import java.util.function.Supplier;

@FunctionalInterface
public interface NonNullSupplier<@NonnullType T> extends Supplier<T> {

    @Override
    T get();

    static <T> NonNullSupplier<T> of(Supplier<@NullableType T> sup) {
        return of(sup, () -> "Unexpected null value from supplier");
    }

    static <T> NonNullSupplier<T> of(Supplier<@NullableType T> sup, NonNullSupplier<String> errorMsg) {
        return () -> {
            T res = sup.get();
            Objects.requireNonNull(res, errorMsg);
            return res;
        };
    }

    default NonNullSupplier<T> lazy() {
        return lazy(this);
    }

    static <T> NonNullSupplier<T> lazy(Supplier<@NonnullType T> sup) {
        return Lazy.of(sup)::get;
    }
}
