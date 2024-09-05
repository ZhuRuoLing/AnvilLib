package dev.anvilcraft.lib.data.provider;

import net.minecraft.advancements.Advancement;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public class AdvancementProvider implements DataProvider, Consumer<Advancement> {
    private final PackOutput output;
    private final List<Advancement> advancements = new ArrayList<>();
    private final String modid;

    public AdvancementProvider(PackOutput output, String modid1) {
        this.output = output;
        this.modid = modid1;
    }

    @Override
    public CompletableFuture<?> run(@NotNull CachedOutput output) {
        List<CompletableFuture<?>> futures = new ArrayList<>();
        for (Advancement advancement : advancements) {
            futures.add(DataProvider.saveStable(output,
                advancement.deconstruct().serializeToJson(),
                this.output
                    .createPathProvider(PackOutput.Target.DATA_PACK, "advancements")
                    .json(advancement.getId())
            ));
        }
        return CompletableFuture.allOf(futures.toArray(CompletableFuture[]::new));
    }

    @Override
    public String getName() {
        return modid + "->Advancements";
    }

    @Override
    public void accept(Advancement advancement) {
        advancements.add(advancement);
    }
}
