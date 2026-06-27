package com.zeydie.slotseffect.bukkit.collectors;

import com.zeydie.slotseffect.api.ArmorEffects;
import com.zeydie.slotseffect.api.ItemEffects;
import com.zeydie.slotseffect.bukkit.data.armors.ArmorSetEffectData;
import com.zeydie.slotseffect.bukkit.data.objects.PotionEffectData;
import com.zeydie.slotseffect.bukkit.utils.ItemUtil;
import com.zeydie.slotseffect.mountcore.SlotsEffect;
import lombok.NonNull;
import lombok.val;
import org.bukkit.NamespacedKey;
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
        @NonNull val armorSetEffectsMap = SlotsEffect.getInstance()
                .getConfigurationModule()
                .getArmorSetsEffects();

        if (armorSetEffectsMap.isEmpty()) return;

        // Получаем компоненты брони игрока один раз
        @NonNull val playerArmorComponents = ItemUtil.getArmorComponents(player);

        for (List<ArmorSetEffectData> setList : armorSetEffectsMap.values()) {
            for (@NonNull val setData : setList) {
                if (isArmorSetComplete(setData, playerArmorComponents)) {
                    List<PotionEffect> setEffects = getSetStaticEffects(setData);
                    mergeEffects(desired, setEffects);
                }
            }
        }
    }

    private static boolean isArmorSetComplete(@NonNull ArmorSetEffectData setData,
                                              @NonNull Map<NamespacedKey, Integer> playerComponents) {

        @NonNull val required = setData.getEquipmentSlotsWithComponents();
        if (required.isEmpty()) return false;

        for (@NonNull val entry : required.entrySet()) {
            NamespacedKey requiredComponent = entry.getValue();

            // Проверяем наличие компонента на броне (независимо от слота, если не критично)
            if (!playerComponents.containsKey(requiredComponent)) {
                return false;
            }
        }
        return true;
    }

    private static List<PotionEffect> getStaticItemEffects(Player player, ItemStack item, int slot) {
        // Используем существующую логику из ItemEffects
        return ItemEffects.getStaticEffects(player, item, slot);
    }

    private static List<PotionEffect> getStaticArmorEffects(ItemStack item, int slot) {
        return ArmorEffects.getStaticEffects(item, slot);
    }

    private static List<PotionEffect> getSetStaticEffects(@NonNull ArmorSetEffectData setData) {
        if (setData.getStaticEffects() == null) return List.of();

        return setData.getStaticEffects().stream()
                .map(PotionEffectData::createPotionEffect)
                .filter(java.util.Objects::nonNull)
                .toList();
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