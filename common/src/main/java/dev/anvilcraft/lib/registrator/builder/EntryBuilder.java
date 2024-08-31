package dev.anvilcraft.lib.registrator.builder;

import dev.anvilcraft.lib.registrator.AbstractRegistrator;
import dev.anvilcraft.lib.registrator.entry.RegistryEntry;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public abstract class EntryBuilder<T> {
    protected final AbstractRegistrator registrator;
    protected final String id;

    protected EntryBuilder(AbstractRegistrator registrator, String id) {
        this.registrator = registrator;
        this.id = id;
    }

    public abstract T build();

    @SuppressWarnings("UnusedReturnValue")
    public abstract RegistryEntry<T> register();

    public ResourceLocation getId() {
        return this.registrator.of(this.id);
    }

    protected static @NotNull String toTitleCase(@NotNull String input) {
        // 使用下划线分割字符串
        String[] parts = input.split("_");
        StringBuilder result = new StringBuilder();

        for (String part : parts) {
            // 将每个单词的首字母转为大写，其余字母保持原样
            if (!part.isEmpty()) {
                result.append(Character.toUpperCase(part.charAt(0)));
                result.append(part.substring(1));
            }
            result.append(" ");
        }

        // 删除最后一个多余的空格
        return result.toString().trim();
    }
}
