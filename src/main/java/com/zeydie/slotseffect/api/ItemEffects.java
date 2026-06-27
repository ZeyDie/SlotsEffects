package com.zeydie.slotseffect.api;

import com.google.common.collect.Maps;
import com.zeydie.slotseffect.bukkit.data.items.ItemEffectData;
import com.zeydie.slotseffect.bukkit.utils.BukkitUtil;
import com.zeydie.slotseffect.bukkit.utils.ItemUtil;
import lombok.NonNull;
import lombok.val;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public final class ItemEffects {
    public static @NotNull Map<NamespacedKey, List<ItemEffectData>> itemsEffects = Maps.newHashMap();

    public static void applyAttackerEffects(@NonNull final LivingEntity attackerLivingEntity, @NonNull final ItemStack itemstack, @NonNull final EquipmentSlot equipmentSlot) {
        if (attackerLivingEntity instanceof final Player player) {
            @NonNull val attackerEffects = getAttackerEffects(itemstack, equipmentSlot);

            if (attackerEffects.isEmpty())
                return;

            for (@NonNull val potionEffect : attackerEffects)
                Effects.applyEffect(player, potionEffect);

            attackerEffects.clear();
        }
    }

    public static void applyVictimEffects(@NonNull final LivingEntity victimLivingEntity, @NonNull final ItemStack itemstack) {
        if (victimLivingEntity instanceof final Player player) {
            @NonNull val victimEffects = getVictimEffects(itemstack);

            if (victimEffects.isEmpty())
                return;

            for (@NonNull val potionEffect : victimEffects)
                Effects.applyEffect(player, potionEffect);

            victimEffects.clear();
        }
    }

    public static @NotNull List<PotionEffect> getStaticEffects(@NonNull final Player player, @NonNull final ItemStack itemstack, final int slot) {
        @NonNull val component = ItemUtil.getComponent(itemstack);

        if (component == null)
            return List.of();

        @NonNull val potionEffects = new ArrayList<PotionEffect>();

        @Nullable val effects = itemsEffects.get(component);

        if (effects == null)
            return List.of();

        for (@NonNull val effectData : effects) {
            @Nullable val slots = effectData.getSlots();

            if (slots.contains(EquipmentSlot.HAND.name())) {
                if (!player.getInventory().getItemInMainHand().equals(itemstack))
                    continue;
            } else if (slots.contains(EquipmentSlot.OFF_HAND.name())) {
                if (!player.getInventory().getItemInOffHand().equals(itemstack))
                    continue;
            } else if (
                    slots != null && (
                            !slots.contains("*")
                                    && !slots.contains("ALL")
                                    && !slots.contains("SLOT_" + slot)
                    )
            )
                continue;

            for (@NonNull val data : effectData.getStaticEffects()) {
                @NonNull val effect = data.createPotionEffect();

                if (effect != null)
                    potionEffects.add(effect);
            }
        }

        return potionEffects;
    }

    private static @NotNull List<PotionEffect> getAttackerEffects(@NonNull final ItemStack itemstack, @Nullable final EquipmentSlot equipmentSlot) {
        @NonNull val component = ItemUtil.getComponent(itemstack);

        if (component == null)
            return List.of();

        @NonNull val potionEffects = new ArrayList<PotionEffect>();

        @Nullable val effects = itemsEffects.get(component);

        if (effects == null)
            return List.of();

        for (@NonNull val effectData : effects) {
            if (equipmentSlot != null) {
                @Nullable val slots = effectData.getSlots();

                if (
                        slots != null && (
                                !slots.contains("*")
                                        && !slots.contains("ALL")
                                        && !slots.contains(equipmentSlot.name())
                        )
                )
                    continue;
            }

            for (@NonNull val data : effectData.getAttackerEffects()) {
                @NonNull val effect = data.createPotionEffect();

                if (effect != null && BukkitUtil.isGoodRandom(data.chance()))
                    potionEffects.add(effect);
            }
        }

        return potionEffects;
    }

    private static @NotNull List<PotionEffect> getVictimEffects(@NonNull final ItemStack itemstack) {
        @NonNull val component = ItemUtil.getComponent(itemstack);

        if (component == null)
            return List.of();

        @NonNull val potionEffects = new ArrayList<PotionEffect>();

        @Nullable val effects = itemsEffects.get(component);

        if (effects == null)
            return List.of();

        for (@NonNull val effectData : effects) {
            for (@NonNull val data : effectData.getVictimEffects()) {
                @NonNull val effect = data.createPotionEffect();

                if (effect != null && BukkitUtil.isGoodRandom(data.chance()))
                    potionEffects.add(effect);
            }
        }

        return potionEffects;
    }
}