/*
 *
 *  * Original work copyright (c) 2019 tterrag1098 (Registrate)
 *  * Additional modifications copyright (c) 2026 Anvil-Dev (AnvilLib-Registrum)
 *  *
 *  * This Source Code Form is subject to the terms of the Mozilla Public
 *  * License, v. 2.0. If a copy of the MPL was not distributed with this
 *  * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *  *
 *  * Original File: https://github.com/tterrag1098/Registrate/blob/1.21.5/dev/D:/Projects/repos/AnvilLib/module.registrum/src/main/java/dev/anvilcraft/lib/v2/registrum/util/nullness/NullableSupplier.java
 *
 */

package dev.anvilcraft.lib.v2.registrum.util.nullness;

import java.util.Objects;
import java.util.function.Supplier;

@Deprecated
public interface NullableSupplier<@NullableType T> extends Supplier<T> {

    @Override
    T get();

    default T getNonNull() {
        return getNonNull(() -> "Unexpected null value from supplier");
    }

    default T getNonNull(NonNullSupplier<String> errorMsg) {
        T res = get();
        Objects.requireNonNull(res, errorMsg);
        return res;
    }

    default NonNullSupplier<T> asNonNull() {
        return () -> getNonNull();
    }

    default NonNullSupplier<T> asNonNull(NonNullSupplier<String> errorMsg) {
        return () -> getNonNull(errorMsg);
    }
}
