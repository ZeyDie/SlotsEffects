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

    public static void applyHitEffects(@NonNull final Player player, @NonNull final ItemStack itemstack, @NonNull final EquipmentSlot equipmentSlot) {
        @NonNull val hitEffects = getArmorHitEffects(itemstack, equipmentSlot);

        hitEffects.addAll(getArmorSetHitEffects(itemstack, equipmentSlot));

        if (hitEffects.isEmpty())
            return;

        for (@NonNull val potionEffect : hitEffects) {
            if (equipmentSlot == null)
                return;

            Effects.applyEffect(player, potionEffect);
        }

        hitEffects.clear();
    }

    public static @NotNull List<PotionEffect> getStaticEffects(@NonNull final ItemStack itemstack, final int slot) {
        @Nullable val component = ItemUtil.getComponent(itemstack);

        if (component == null)
            return List.of();

        @NonNull val potionEffects = new ArrayList<PotionEffect>();

        @Nullable val effects = armorEffects.get(component);

        if (effects == null)
            return List.of();

        for (@NonNull val effectData : effects) {
            @Nullable val equipmentSlot = effectData.getEquipmentSlot();

            if (equipmentSlot != null && equipmentSlot != BukkitUtil.getEquipmentOfArmorSlot(slot))
                continue;

            for (@NonNull val data : effectData.getStaticEffects()) {
                @NonNull val effect = data.createPotionEffect();

                if (effect != null)
                    potionEffects.add(effect);
            }
        }

        return potionEffects;
    }

    private static @NotNull List<PotionEffect> getArmorHitEffects(@NonNull final ItemStack itemstack, @NonNull final EquipmentSlot equipmentSlot) {
        @NonNull val component = ItemUtil.getComponent(itemstack);

        if (component == null)
            return List.of();

        @NonNull val potionEffects = new ArrayList<PotionEffect>();

        @Nullable val effects = armorEffects.get(component);

        if (effects == null)
            return List.of();

        for (@NonNull val effectData : effects) {
            @Nullable val effectDataEquipmentSlot = effectData.getEquipmentSlot();

            if (equipmentSlot != null && effectDataEquipmentSlot != equipmentSlot)
                continue;

            for (@NonNull val data : effectData.getHitEffects()) {
                @NonNull val effect = data.createPotionEffect();

                if (effect != null && BukkitUtil.isGoodRandom(data.chance()))
                    potionEffects.add(effect);
            }
        }

        return potionEffects;
    }

    private static @NotNull List<PotionEffect> getArmorSetHitEffects(@NonNull final ItemStack itemstack, @NonNull final EquipmentSlot equipmentSlot) {
        @NonNull val component = ItemUtil.getComponent(itemstack);

        if (component == null)
            return List.of();

        @NonNull val potionEffects = new ArrayList<PotionEffect>();

        @Nullable val effects = armorSetsEffects.get(component);

        if (effects == null)
            return List.of();

        for (@NonNull val effectData : effects) {
            @Nullable val effectDataEquipmentSlotsWithComponents = effectData.getEquipmentSlotsWithComponents();

            if (equipmentSlot != null && !effectDataEquipmentSlotsWithComponents.containsKey(equipmentSlot))
                continue;

            for (@NonNull val data : effectData.getHitEffects()) {
                @NonNull val effect = data.createPotionEffect();

                if (effect != null && BukkitUtil.isGoodRandom(data.chance()))
                    potionEffects.add(effect);
            }
        }

        return potionEffects;
    }
}