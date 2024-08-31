package dev.anvilcraft.lib.registrator;

import dev.anvilcraft.lib.registrator.builder.EntryBuilder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class BuilderManager implements Iterable<Registry<?>> {
    protected final List<Registry<?>> registries = Collections.synchronizedList(new ArrayList<>());
    protected final Map<Registry<?>, List<EntryBuilder<?>>> builders = Collections.synchronizedMap(new HashMap<>());

    BuilderManager() {
    }

    private <T> void addRegistry(Registry<T> registry) {
        if (this.registries.contains(registry)) return;
        this.registries.add(registry);
        this.registries.sort(BuilderManager::compare);
    }

    public <T> void addBuilder(Registry<T> registry, EntryBuilder<? extends T> builder) {
        List<EntryBuilder<?>> builderList = this.builders.getOrDefault(registry, Collections.synchronizedList(new ArrayList<>()));
        builderList.add(builder);
        this.addRegistry(registry);
    }

    @SuppressWarnings("unchecked")
    protected <V, T extends V> List<EntryBuilder<T>> getBuilders(Registry<V> registry) {
        List<EntryBuilder<?>> builderList = this.builders.getOrDefault(registry, Collections.synchronizedList(new ArrayList<>()));
        return builderList.stream()
            .map(builder -> (EntryBuilder<T>) builder)
            .toList();
    }

    @NotNull
    @Override
    public Iterator<Registry<?>> iterator() {
        return this.registries.iterator();
    }

    private static final Map<Registry<?>, Integer> REGISTRY_PRIORITY = Map.of(
        BuiltInRegistries.BLOCK, 1
    );

    private static int getRegistryPriority(Registry<?> registry) {
        return REGISTRY_PRIORITY.getOrDefault(registry, 100);
    }

    public static int compare(Registry<?> o1, Registry<?> o2) {
        int priority1 = BuilderManager.getRegistryPriority(o1);
        int priority2 = BuilderManager.getRegistryPriority(o2);
        return priority1 - priority2;
    }
}
