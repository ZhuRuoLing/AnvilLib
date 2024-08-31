package dev.anvilcraft.lib.registrator.forge;

import dev.anvilcraft.lib.AnvilLib;
import dev.anvilcraft.lib.registrator.Registrator;
import dev.anvilcraft.lib.registrator.builder.EntryBuilder;
import net.minecraft.core.Registry;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.registries.RegisterEvent;
import org.jetbrains.annotations.NotNull;

public class RegistratorImpl extends Registrator {
    private RegistratorImpl(String modid) {
        super(modid);
    }

    @Override
    public void init() {
        MinecraftForge.EVENT_BUS.addListener(this::register);
    }

    public void register(RegisterEvent event) {
        for (Registry<?> registry : this.manager) {
            this.init(event, registry);
        }
    }

    private <T> void init(RegisterEvent event, Registry<T> registry) {
        for (EntryBuilder<T> builder : this.getBuilders(registry)) {
            try {
                event.register(registry.key(), builder.getId(), builder::build);
            } catch (Exception e) {
                if (e instanceof ClassCastException) return;
                AnvilLib.LOGGER.error(e.getMessage(), e);
            }
        }
    }

    @NotNull
    public static Registrator create(String modid) {
        return new RegistratorImpl(modid);
    }
}
