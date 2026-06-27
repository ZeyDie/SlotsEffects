package com.zeydie.slotseffect.bukkit.collectors;

import com.zeydie.slotseffect.api.ArmorEffects;
import com.zeydie.slotseffect.api.ItemEffects;
import lombok.NonNull;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class EffectCollector {

    public static Map<PotionEffectType, PotionEffect> collect(@NonNull Player player) {
        Map<PotionEffectType, PotionEffect> desired = new HashMap<>();

        // Инвентарь (включая руку)
        collectInventoryEffects(player, desired);

        // Броня
        collectArmorEffects(player, desired);

        // Сеты брони
        collectArmorSetEffects(player, desired);

        return desired;
    }

    private static void collectInventoryEffects(Player player, Map<PotionEffectType, PotionEffect> desired) {
        ItemStack[] contents = player.getInventory().getContents();
        for (int slot = 0; slot < contents.length; slot++) {
            ItemStack item = contents[slot];
            if (item == null || item.getType().isAir()) continue;

            List<PotionEffect> effects = getStaticItemEffects(player, item, slot);
            mergeEffects(desired, effects);
        }
    }

    private static void collectArmorEffects(Player player, Map<PotionEffectType, PotionEffect> desired) {
        ItemStack[] armor = player.getInventory().getArmorContents();
        for (int i = 0; i < armor.length; i++) {
            ItemStack item = armor[i];
            if (item == null || item.getType().isAir()) continue;

            List<PotionEffect> effects = getStaticArmorEffects(item, i);
            mergeEffects(desired, effects);
        }
    }

    private static void collectArmorSetEffects(Player player, Map<PotionEffectType, PotionEffect> desired) {
        // TODO: Реализовать логику сетов, если нужно
        // Сейчас пусто, как в оригинале
    }

    private static List<PotionEffect> getStaticItemEffects(Player player, ItemStack item, int slot) {
        // Используем существующую логику из ItemEffects
        return ItemEffects.getStaticEffects(player, item, slot);
    }

    private static List<PotionEffect> getStaticArmorEffects(ItemStack item, int slot) {
        return ArmorEffects.getStaticEffects(item, slot);
    }

    private static void mergeEffects(Map<PotionEffectType, PotionEffect> desired, List<PotionEffect> newEffects) {
        for (PotionEffect effect : newEffects) {
            PotionEffectType type = effect.getType();
            PotionEffect existing = desired.get(type);

            if (existing == null || existing.getAmplifier() < effect.getAmplifier()) {
                desired.put(type, effect);
            }
        }
    }
}