package dev.anvilcraft.lib.data;

import lombok.Getter;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;

import java.util.Optional;

public class TagBuilder<T> {
    @Getter
    private boolean replace = false;
    private final ResourceKey<? extends Registry<T>> resourceKey;
    @Getter
    private final net.minecraft.tags.TagBuilder parent;


    public TagBuilder(
        net.minecraft.tags.TagBuilder builder,
        ResourceKey<? extends Registry<T>> resourceKey
    ) {
        this.parent = builder;
        this.resourceKey = resourceKey;
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    protected ResourceKey<T> key(T element) {
        Registry registry = BuiltInRegistries.REGISTRY.get((ResourceKey) resourceKey);
        if (registry != null) {
            Optional<Holder<T>> key = registry.getResourceKey(element);

            if (key.isPresent()) {
                return (ResourceKey<T>) key.get();
            }
        }

        throw new UnsupportedOperationException(getClass().toString());
    }

    public TagBuilder<T> addTag(TagKey<T> tagKey){
        this.parent.addTag(tagKey.location());
        return this;
    }


    public TagBuilder<T> setReplace(boolean r) {
        this.replace = r;
        return this;
    }

    public TagBuilder<T> add(T element) {
        parent.addElement(key(element).location());
        return this;
    }

    public TagBuilder<T> addOptionalTag(TagKey<T> tag){
        this.parent.addOptionalTag(tag.location());
        return this;
    }


}
