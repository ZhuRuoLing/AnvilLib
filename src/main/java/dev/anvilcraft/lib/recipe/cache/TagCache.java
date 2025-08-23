package dev.anvilcraft.lib.recipe.cache;

import dev.anvilcraft.lib.AnvilLib;
import dev.anvilcraft.lib.recipe.util.InWorldRecipeContext;
import dev.anvilcraft.lib.recipe.util.InWorldRecipeData;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.function.Function;

public class TagCache {
    public static final InWorldRecipeData<TagCache> TAG_CACHE = InWorldRecipeData.of(AnvilLib.of("block_cache"), TagCache::of);
    public Map<ResourceLocation, Tag> tags;

    public TagCache() {
    }

    private static TagCache of(InWorldRecipeContext level, InWorldRecipeData<TagCache> key) {
        return new TagCache();
    }

    public @Nullable Tag getTag(ResourceLocation id) {
        return this.tags.get(id);
    }

    public void putTag(ResourceLocation id, Tag tag) {
        this.tags.put(id, tag);
    }

    public void computeIfAbsent(ResourceLocation id, Function<ResourceLocation, Tag> tag) {
        this.tags.computeIfAbsent(id, tag);
    }
}
