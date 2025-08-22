package dev.anvilcraft.lib.config;

import dev.anvilcraft.lib.util.FormattingUtil;
import lombok.extern.slf4j.Slf4j;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import net.neoforged.neoforge.common.ModConfigSpec;

import javax.annotation.Nullable;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class ConfigManager {
    private final Map<Object, Map.Entry<ModConfig.Type, ModConfigSpec>> configSpecMap = new HashMap<>();

    public <T> T register(T configObj) {
        Class<?> configClass = configObj.getClass();
        ModConfigSpec.Builder builder = new ModConfigSpec.Builder();
        if (configClass.isAnnotationPresent(Config.class)) {
            Config config = configClass.getAnnotation(Config.class);
            String name = config.name();
            ModConfig.Type type = config.type();
            try {
                ConfigManager.register(builder, name, type, null, configObj);
            } catch (IllegalAccessException e) {
                log.error(e.getMessage(), e);
            }
            ModConfigSpec spec = builder.build();
            this.configSpecMap.put(configObj, Map.entry(type, spec));
        }
        return configObj;
    }

    public void register(ModContainer container) {
        for (Map.Entry<Object, Map.Entry<ModConfig.Type, ModConfigSpec>> entry : this.configSpecMap.entrySet()) {
            container.registerConfig(entry.getValue().getKey(), entry.getValue().getValue());
        }
    }

    @OnlyIn(Dist.CLIENT)
    public void registerScreen(ModContainer container) {
        container.registerExtensionPoint(IConfigScreenFactory.class, ConfigurationScreen::new);
    }

    private static void register(
        ModConfigSpec.Builder builder,
        String modId,
        ModConfig.Type type,
        @Nullable String parent,
        Object configObj
    ) throws IllegalAccessException {
        Class<?> configClass = configObj.getClass();
        Field[] fields = configClass.getDeclaredFields();
        for (Field field : fields) {
            String fieldName = FormattingUtil.toLowerCaseUnder(field.getName());
            Object object = field.get(configObj);
            if (field.isAnnotationPresent(Comment.class)) {
                Comment comment = field.getAnnotation(Comment.class);
                String name = fieldName;
                if (parent != null) name = parent + "." + name;
                builder.translation(ConfigData.OPTION_STRING.formatted(modId, name));
                builder.comment(comment.value());
            }
            ConfigManager.define(builder, modId, type, fieldName, field, object);
        }
    }

    public static void define(
        ModConfigSpec.Builder builder,
        String modId,
        ModConfig.Type type,
        String name,
        Field field,
        Object object
    ) throws IllegalAccessException {
        if (object instanceof Number num) {
            ConfigManager.defineInRange(builder, name, field, num);
        } else if (object instanceof Enum<?> enumValue) {
            ConfigManager.defineEnum(builder, name, enumValue);
        } else if (field.isAnnotationPresent(CollapsibleObject.class)) {
            builder.push(name);
            ConfigManager.register(builder, modId, type, name, object);
            builder.pop();
        } else if (object instanceof Boolean bool) {
            builder.define(name, bool.booleanValue());
        } else {
            builder.define(name, object);
        }
    }

    @SuppressWarnings("unchecked")
    public static <E extends Enum<E>> void defineEnum(ModConfigSpec.Builder builder, String name, Enum<?> enumValue) {
        builder.defineEnum(name, (E) enumValue);
    }

    public static void defineInRange(ModConfigSpec.Builder builder, String name, Field field, Number number) {
        if (field.isAnnotationPresent(BoundedDiscrete.class)) {
            BoundedDiscrete discrete = field.getAnnotation(BoundedDiscrete.class);
            switch (number) {
                case Byte value ->
                    builder.defineInRange(name, value, BoundedDiscrete.Util.minByte(discrete), BoundedDiscrete.Util.maxByte(discrete));
                case Short value ->
                    builder.defineInRange(name, value, BoundedDiscrete.Util.minShort(discrete), BoundedDiscrete.Util.maxShort(discrete));
                case Integer value ->
                    builder.defineInRange(name, value, BoundedDiscrete.Util.minInt(discrete), BoundedDiscrete.Util.maxInt(discrete));
                case Long value ->
                    builder.defineInRange(name, value, BoundedDiscrete.Util.minLong(discrete), BoundedDiscrete.Util.maxLong(discrete));
                case Float value ->
                    builder.defineInRange(name, value, BoundedDiscrete.Util.minFloat(discrete), BoundedDiscrete.Util.maxFloat(discrete));
                case Double value ->
                    builder.defineInRange(name, value, BoundedDiscrete.Util.minDouble(discrete), BoundedDiscrete.Util.maxDouble(discrete));
                default -> builder.define(name, number);
            }
        } else {
            builder.define(name, number);
        }
    }
}
