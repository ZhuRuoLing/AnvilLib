package dev.anvilcraft.lib.registrator.builder;

import dev.anvilcraft.lib.registrator.AbstractRegistrator;
import dev.anvilcraft.lib.registrator.entry.EnchantmentEntry;
import dev.anvilcraft.lib.registrator.entry.RegistryEntry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;

import java.util.EnumSet;

public class EnchantmentBuilder<T extends Enchantment> extends EntryBuilder<T> {
    private final EnchantmentEntry<T> entry = new EnchantmentEntry<>();
    private Enchantment.Rarity rarity = Enchantment.Rarity.COMMON;
    private final EnchantmentCategory category;
    private final EnumSet<EquipmentSlot> slots = EnumSet.noneOf(EquipmentSlot.class);
    private final EnchantmentFactory<T> factory;
    private String name = "";

    public EnchantmentBuilder(
        AbstractRegistrator registrator,
        String id,
        EnchantmentCategory category,
        EnchantmentFactory<T> factory
    ) {
        super(registrator, id);
        this.category = category;
        this.factory = factory;
        defaultName();
    }

    @Override
    public T build() {
        T enc = this.factory.create(rarity, category, slots.toArray(new EquipmentSlot[0]));
        entry.set(enc);
        return null;
    }

    @Override
    public RegistryEntry<T> register() {
        this.registrator.addBuilder(BuiltInRegistries.ENCHANTMENT, this);
        this.registrator.lang("enchantment." + registrator.getModid() + "." + id, name);
        return null;
    }

    @Override
    public RegistryEntry<T> entry() {
        return this.entry;
    }

    public EnchantmentBuilder<T> slot(EquipmentSlot slot){
        this.slots.add(slot);
        return this;
    }

    public EnchantmentBuilder<T> rarity(Enchantment.Rarity rarity){
        this.rarity = rarity;
        return this;
    }

    public EnchantmentBuilder<T> defaultName(){
        return name(id.substring(0,1).toUpperCase() + id.substring(1));
    }

    public EnchantmentBuilder<T> name(String name){
        this.name = name;
        return this;
    }

    @FunctionalInterface
    public interface EnchantmentFactory<T extends Enchantment>{
        T create(Enchantment.Rarity rarity, EnchantmentCategory category, EquipmentSlot[] applicableSlots);
    }
}
