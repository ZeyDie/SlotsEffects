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
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class EffectCollector {

    public static Map<PotionEffectType, PotionEffect> collect(@NonNull Player player) {
        Map<PotionEffectType, PotionEffect> desired = new HashMap<>();

        collectInventoryEffects(player, desired);
        collectArmorEffects(player, desired);
        collectArmorSetEffects(player, desired);

        return desired;
    }

    private static void collectInventoryEffects(Player player, Map<PotionEffectType, PotionEffect> desired) {
        ItemStack[] contents = player.getInventory().getContents();
        for (int slot = 0; slot < contents.length; slot++) {
            ItemStack item = contents[slot];
            if (item == null || item.getType().isAir()) continue;

            List<PotionEffect> effects = ItemEffects.getStaticEffects(player, item, slot);
            mergeEffects(desired, effects);
        }
    }

    private static void collectArmorEffects(Player player, Map<PotionEffectType, PotionEffect> desired) {
        ItemStack[] armor = player.getInventory().getArmorContents();
        for (int i = 0; i < armor.length; i++) {
            ItemStack item = armor[i];
            if (item == null || item.getType().isAir()) continue;

            List<PotionEffect> effects = ArmorEffects.getStaticEffects(item, i);
            mergeEffects(desired, effects);
        }
    }

    private static void collectArmorSetEffects(Player player, Map<PotionEffectType, PotionEffect> desired) {
        @NonNull val armorSetEffectsMap = SlotsEffect.getInstance()
                .getConfigurationModule()
                .getArmorSetsEffects();

        if (armorSetEffectsMap.isEmpty()) return;

        for (List<ArmorSetEffectData> setList : armorSetEffectsMap.values()) {
            for (@NonNull val setData : setList) {
                if (isArmorSetComplete(player, setData)) {
                    List<PotionEffect> setEffects = getSetStaticEffects(setData);
                    mergeEffects(desired, setEffects);
                }
            }
        }
    }

    private static boolean isArmorSetComplete(@NonNull Player player, @NonNull ArmorSetEffectData setData) {
        @NonNull val required = setData.getEquipmentSlotsWithComponents();
        if (required.isEmpty()) return false;

        for (@NonNull val entry : required.entrySet()) {
            @NonNull EquipmentSlot slot = entry.getKey();
            @NonNull NamespacedKey component = entry.getValue();

            ItemStack itemInSlot = getItemInSlot(player, slot);

            if (itemInSlot == null || !ItemUtil.hasComponent(itemInSlot, component)) {
                return false;
            }
        }

        return true;
    }

    private static @Nullable ItemStack getItemInSlot(@NonNull Player player, @NonNull EquipmentSlot slot) {
        return switch (slot) {
            case HEAD -> player.getInventory().getHelmet();
            case CHEST -> player.getInventory().getChestplate();
            case LEGS -> player.getInventory().getLeggings();
            case FEET -> player.getInventory().getBoots();
            case OFF_HAND -> player.getInventory().getItemInOffHand();
            default -> null;
        };
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