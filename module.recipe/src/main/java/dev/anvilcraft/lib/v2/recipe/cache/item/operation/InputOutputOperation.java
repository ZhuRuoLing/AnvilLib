package dev.anvilcraft.lib.v2.recipe.cache.item.operation;

import dev.anvilcraft.lib.v2.recipe.cache.item.ICacheElement;

import java.util.Set;

/**
 * 输入输出操作记录类
 */
public record InputOutputOperation(Set<ICacheElement> elements) {
}
