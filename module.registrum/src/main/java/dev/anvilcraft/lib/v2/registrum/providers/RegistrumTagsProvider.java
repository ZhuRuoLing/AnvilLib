/*
 *
 *  * Original work copyright (c) 2019 tterrag1098 (Registrate)
 *  * Additional modifications copyright (c) 2026 Anvil-Dev (AnvilLib-Registrum)
 *  *
 *  * This Source Code Form is subject to the terms of the Mozilla Public
 *  * License, v. 2.0. If a copy of the MPL was not distributed with this
 *  * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *  *
 *  * Original File: https://github.com/tterrag1098/Registrate/blob/1.21.5/dev/D:/Projects/repos/AnvilLib/module.registrum/src/main/java/dev/anvilcraft/lib/v2/registrum/providers/RegistrateTagsProvider.java
 *
 */

package dev.anvilcraft.lib.v2.registrum.providers;

import dev.anvilcraft.lib.v2.registrum.AbstractRegistrum;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.Registry;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.IntrinsicHolderTagsProvider;
import net.minecraft.data.tags.TagAppender;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.neoforged.fml.LogicalSide;

import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

public interface RegistrumTagsProvider<T> extends RegistrumLookupFillerProvider {

    TagAppender<E,T> addTag(TagKey<E> tag);

    CompletableFuture<TagsProvider.TagLookup<T>> contentsGetter();

    ResourceKey<? extends Registry<T>> registry();

    class Impl<T> extends TagsProvider<T> implements RegistrumTagsProvider<T> {
        private final AbstractRegistrum<?> owner;
        private final ProviderType<? extends Impl<T>> type;
        private final String name;

        public Impl(
            AbstractRegistrum<?> owner,
            ProviderType<? extends Impl<T>> type,
            String name,
            PackOutput packOutput,
            ResourceKey<? extends Registry<T>> registryIn,
            CompletableFuture<HolderLookup.Provider> registriesLookup
        ) {
            super(packOutput, registryIn, registriesLookup, owner.getModid());

            this.owner = owner;
            this.type = type;
            this.name = name;
        }

        @Override
        public String getName() {
            return "Tags (%s)".formatted(name);
        }

        @Override
        protected void addTags(HolderLookup.Provider provider) {
            owner.genData(type, this);
        }

        @Override
        public LogicalSide getSide() {
            return LogicalSide.SERVER;
        }

        @Override
        public TagAppender<T> addTag(TagKey<T> tag) {
            return super.tag(tag);
        }

        @Override
        public CompletableFuture<HolderLookup.Provider> getFilledProvider() {
            return createContentsProvider();
        }

        @Override
        public ResourceKey<? extends Registry<T>> registry() {
            return registryKey;
        }

    }

    class IntrinsicImpl<T> extends IntrinsicHolderTagsProvider<T> implements RegistrumTagsProvider<T> {
        private final AbstractRegistrum<?> owner;
        private final ProviderType<? extends IntrinsicImpl<T>> type;
        private final String name;

        public IntrinsicImpl(
            AbstractRegistrum<?> owner,
            ProviderType<? extends IntrinsicImpl<T>> type,
            String name,
            PackOutput packOutput,
            ResourceKey<? extends Registry<T>> registryIn,
            CompletableFuture<HolderLookup.Provider> registriesLookup,
            Function<T, ResourceKey<T>> keyExtractor
        ) {
            super(packOutput, registryIn, registriesLookup, keyExtractor, owner.getModid());

            this.owner = owner;
            this.type = type;
            this.name = name;
        }

        @Override
        public String getName() {
            return "Tags (%s)".formatted(name);
        }

        @Override
        protected void addTags(HolderLookup.Provider provider) {
            owner.genData(type, this);
        }

        @Override
        public LogicalSide getSide() {
            return LogicalSide.SERVER;
        }

        @Override
        public IntrinsicTagAppender<T> addTag(TagKey<T> tag) {
            return super.tag(tag);
        }

        @Override
        public CompletableFuture<HolderLookup.Provider> getFilledProvider() {
            return createContentsProvider();
        }

        @Override
        public ResourceKey<? extends Registry<T>> registry() {
            return registryKey;
        }

    }
}
