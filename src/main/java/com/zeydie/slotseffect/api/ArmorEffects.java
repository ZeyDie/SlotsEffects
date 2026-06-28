package com.zeydie.slotseffect.api;

import com.google.common.collect.Maps;
import com.zeydie.slotseffect.bukkit.data.armors.ArmorEffectData;
import com.zeydie.slotseffect.bukkit.data.armors.ArmorSetEffectData;
import com.zeydie.slotseffect.bukkit.utils.BukkitUtil;
import com.zeydie.slotseffect.bukkit.utils.ItemUtil;
import lombok.NonNull;
import lombok.val;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public final class ArmorEffects {
    public static final @NotNull Map<NamespacedKey, List<ArmorEffectData>> armorEffects = Maps.newHashMap();
    public static final @NotNull Map<NamespacedKey, List<ArmorSetEffectData>> armorSetsEffects = Maps.newHashMap();

    public static @NotNull List<PotionEffect> getStaticEffects(@NonNull final ItemStack itemstack, final int slot) {
        @Nullable val component = ItemUtil.getComponent(itemstack);

        if (component == null)
            return List.of();

        @NonNull val potionEffects = new ArrayList<PotionEffect>();
        @Nullable val effects = armorEffects.get(component);

        if (effects == null)
            return List.of();

        @Nullable val requiredSlot = BukkitUtil.getEquipmentOfArmorSlot(slot);

        for (@NonNull val effectData : effects) {
            @Nullable val equipmentSlot = effectData.getEquipmentSlot();

            if (equipmentSlot != null && equipmentSlot != requiredSlot)
                continue;

            for (@NonNull val data : effectData.getStaticEffects()) {
                @NonNull val effect = data.createPotionEffect();

                if (effect != null)
                    potionEffects.add(effect);
            }
        }

        return potionEffects;
    }

    public static @NotNull List<PotionEffect> getArmorHitEffects(@NonNull final ItemStack itemstack, @NonNull final EquipmentSlot equipmentSlot) {
        @NonNull val component = ItemUtil.getComponent(itemstack);

        if (component == null)
            return List.of();

        @NonNull val potionEffects = new ArrayList<PotionEffect>();
        @Nullable val effects = armorEffects.get(component);

        if (effects == null)
            return List.of();

        for (@NonNull val effectData : effects) {
            @Nullable val effectDataEquipmentSlot = effectData.getEquipmentSlot();

            if (effectDataEquipmentSlot != null && effectDataEquipmentSlot != equipmentSlot)
                continue;

            for (@NonNull val data : effectData.getHitEffects()) {
                @NonNull val effect = data.createPotionEffect();

                if (effect != null && BukkitUtil.isGoodRandom(data.chance()))
                    potionEffects.add(effect);
            }
        }

        return potionEffects;
    }

    public static @NotNull List<PotionEffect> getArmorSetHitEffects(@NonNull final Player player) {
        @NonNull val potionEffects = new ArrayList<PotionEffect>();
        @NonNull val armorSetEffectsMap = armorSetsEffects;

        if (armorSetEffectsMap.isEmpty()) return potionEffects;

        for (List<ArmorSetEffectData> setList : armorSetEffectsMap.values()) {
            for (@NonNull val setData : setList) {
                // Полная проверка комплекта
                if (isArmorSetComplete(player, setData)) {
                    for (@NonNull val data : setData.getHitEffects()) {
                        @NonNull val effect = data.createPotionEffect();

                        if (effect != null && BukkitUtil.isGoodRandom(data.chance()))
                            potionEffects.add(effect);
                    }
                }
            }
        }

        return potionEffects;
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
}