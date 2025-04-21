package dev.anvilcraft.lib.integration;

import com.electronwill.nightconfig.core.UnmodifiableConfig;
import com.google.common.collect.Multimap;
import com.google.common.collect.MultimapBuilder;
import dev.anvilcraft.lib.AnvilLib;
import lombok.extern.slf4j.Slf4j;
import net.neoforged.bus.api.Event;
import net.neoforged.fml.ModList;
import net.neoforged.fml.event.IModBusEvent;
import net.neoforged.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.neoforged.fml.loading.FMLLoader;
import net.neoforged.fml.loading.LoadingModList;
import net.neoforged.fml.loading.moddiscovery.ModFileInfo;
import net.neoforged.fml.loading.moddiscovery.ModInfo;
import net.neoforged.fml.loading.progress.ProgressMeter;
import net.neoforged.fml.loading.progress.StartupNotificationManager;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforgespi.language.IModInfo;
import net.neoforged.neoforgespi.language.ModFileScanData;
import org.apache.maven.artifact.versioning.ArtifactVersion;
import org.apache.maven.artifact.versioning.InvalidVersionSpecificationException;
import org.jetbrains.annotations.NotNull;

import java.lang.annotation.ElementType;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Slf4j
public class IntegrationManager {
    private final Multimap<String, IntegrationInstance> instances = MultimapBuilder.hashKeys().hashSetValues().build();
    public static final String INTEGRATION_NAME = "L" + Integrations.class.getName().replace(".", "/") + ";";

    public void compileContent() {
        ProgressMeter meter = StartupNotificationManager.addProgressBar("Load Integrations", LoadingModList.get().getModFiles().size());
        for (ModFileInfo modFile : LoadingModList.get().getModFiles()) {
            meter.increment();
            @SuppressWarnings("UnstableApiUsage")
            ModFileScanData scanData = modFile.getFile().getScanResult();
            for (ModFileScanData.AnnotationData annotation : scanData.getAnnotations()) {
                if (annotation.annotationType().getDescriptor().equals(INTEGRATION_NAME) && annotation.targetType() == ElementType.TYPE) {
                    String modid = (String) annotation.annotationData().get("value");
                    String version = (String) annotation.annotationData().get("version");
                    //noinspection unchecked
                    Class<? extends Event> event = (Class<? extends Event>) annotation.annotationData().get("event");
                    if (null == version) version = "*";
                    if (null == event) event = FMLLoadCompleteEvent.class;
                    log.info("Considering integration {} for {}", annotation.memberName(), modid);
                    try {
                        IntegrationInstance instance = new IntegrationInstance(
                            modid,
                            ModVersionRange.of(version),
                            annotation.memberName(),
                            event
                        );
                        this.put(modid, instance);
                    } catch (InvalidVersionSpecificationException e) {
                        log.error("Invalid version specification for integration {}", annotation.memberName(), e);
                    }
                }
            }
        }
        this.loadEvent();
        this.loadOld();
        StartupNotificationManager.popBar(meter);
    }

    public void put(String modid, IntegrationInstance instance) {
        this.instances.put(modid, instance);
    }

    private void loadEvent() {
        Set<Class<? extends Event>> events = new HashSet<>();
        for (IntegrationInstance instance : instances.values()) {
            events.add(instance.getEvent());
        }
        for (Class<?> event : events) {
            if (!IModBusEvent.class.isAssignableFrom(event)) {
                NeoForge.EVENT_BUS.addListener(this::loadEvent);
            }
        }
    }

    private <T extends Event> void loadEvent(@NotNull T event) {
        this.loadAllIntegrations(event.getClass());
        if (FMLLoader.getDist().isClient()) {
            this.loadAllClientIntegrations(event.getClass());
        }
    }

    private void loadOld() {
        for (IModInfo mod : ModList.get().getMods()) {
            Map<String, Object> modProperties = mod.getModProperties();
            for (Map.Entry<String, Object> entry : modProperties.entrySet()) {
                if (!entry.getKey().equals(AnvilLib.MOD_ID)) continue;
                if (!(entry.getValue() instanceof UnmodifiableConfig anvilConfig)) return;
                for (UnmodifiableConfig.Entry entry1 : anvilConfig.entrySet()) {
                    if (!entry1.getKey().equals("integrations")) continue;
                    if (!(entry1.getValue() instanceof UnmodifiableConfig config)) return;
                    this.loadOldIntegrations(config);
                }
            }
        }
    }

    private void loadOldIntegrations(@NotNull UnmodifiableConfig integrations) {
        for (UnmodifiableConfig.Entry entry2 : integrations.entrySet()) {
            String modid = entry2.getKey();
            Object value = entry2.getValue();
            if (value instanceof String string) {
                this.instances.put(modid, IntegrationInstance.of(modid, string));
            } else if (value instanceof List<?> list) {
                for (Object object : list) {
                    if (!(object instanceof String string)) continue;
                    this.instances.put(modid, IntegrationInstance.of(modid, string));
                }
            }
        }
    }

    public void load(String modid, ArtifactVersion version, Class<?> event) {
        for (IntegrationInstance instance : instances.get(modid)) {
            if (!instance.getEvent().isAssignableFrom(event)) continue;
            if (!instance.getVersion().containsVersion(version)) continue;
            instance.newInstance();
            log.info("Loading integration {} for {}.", instance.instance(), modid);
            instance.invoke();
        }
    }

    public void loadClient(String modid, ArtifactVersion version, Class<?> event) {
        for (IntegrationInstance instance : instances.get(modid)) {
            if (!instance.getEvent().isAssignableFrom(event)) continue;
            if (!instance.getVersion().containsVersion(version)) continue;
            instance.newInstance();
            log.info("Loading client integration {} for {}.", instance.instance(), modid);
            instance.invokeClient();
        }
    }

    public void loadAllIntegrations(Class<?> event) {
        for (ModInfo mod : LoadingModList.get().getMods()) {
            if (instances.containsKey(mod.getModId())) {
                load(mod.getModId(), mod.getVersion(), event);
            }
        }
    }

    public void loadAllClientIntegrations(Class<?> event) {
        for (ModInfo mod : LoadingModList.get().getMods()) {
            if (instances.containsKey(mod.getModId())) {
                loadClient(mod.getModId(), mod.getVersion(), event);
            }
        }
    }
}
