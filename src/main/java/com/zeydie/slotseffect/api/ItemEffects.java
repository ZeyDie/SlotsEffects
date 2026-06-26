package com.zeydie.slotseffect.api;

import com.google.common.base.Function;
import com.zeydie.slotseffect.bukkit.data.EffectData;
import com.zeydie.slotseffect.bukkit.data.PotionEffectData;
import com.zeydie.slotseffect.bukkit.utils.BukkitUtil;
import com.zeydie.slotseffect.bukkit.utils.ItemUtil;
import com.zeydie.slotseffect.mountcore.SlotsEffect;
import lombok.NonNull;
import lombok.val;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public final class ItemEffects {
    public static void applyEffect(@NonNull final Player player, @NonNull final PotionEffect potionEffect) {
        BukkitUtil.runTaskLater(() -> player.addPotionEffect(potionEffect, true));
    }

    public static void applyEffects(@NonNull final Player player, @NonNull final ItemStack itemstack, final int slot) {
        /*@NonNull val components = ItemUtil.getComponents(itemstack);

        if (components.isEmpty()) return;

        @NonNull val itemEffects = SlotsEffect.getInstance()
                .getConfigurationModule()
                .getItemsEffects();*/

        @NonNull val staticEffects = getStaticEffects(itemstack, slot);

        staticEffects.forEach(potionEffect -> applyEffect(player, potionEffect));
        staticEffects.clear();
    }

    public static void applyAtackerEffects(@NonNull final LivingEntity attackerLivingEntity, @NonNull final ItemStack itemstack, @NonNull final EquipmentSlot equipmentSlot) {
        if (attackerLivingEntity instanceof final Player player) {
            @NonNull val attackerEffects = getAttackerEffects(itemstack, equipmentSlot);

            attackerEffects.forEach(potionEffect -> applyEffect(player, potionEffect));
            attackerEffects.clear();
        }
    }

    public static void applyVictimEffects(@NonNull final LivingEntity victimLivingEntity, @NonNull final ItemStack itemstack) {
        if (victimLivingEntity instanceof final Player player) {
            @NonNull val victimEffects = getVictimEffects(itemstack);

            victimEffects.forEach(potionEffect -> applyEffect(player, potionEffect));
            victimEffects.clear();
        }
    }

    public static @NotNull List<PotionEffect> getStaticEffects(@NonNull final ItemStack itemstack, final int slot) {
        @NonNull val components = ItemUtil.getComponents(itemstack);

        if (components.isEmpty()) return List.of();

        @NonNull val potionEffects = new ArrayList<PotionEffect>();

        @NonNull val itemEffects = SlotsEffect.getInstance()
                .getConfigurationModule()
                .getItemsEffects();

        components.forEach(
                component -> {
                    itemEffects.get(component)
                            .stream()
                            .filter(
                                    effectData -> {
                                        @NonNull val slots = effectData.getSlots();

                                        if (slots.contains("ALL") || slots.contains("*"))
                                            return true;

                                        return slots.contains("SLOT_" + slot);
                                    }
                            )
                            .forEach(
                                    effectData -> effectData.getStaticEffects()
                                            .forEach(
                                                    potionEffectData ->
                                                    {
                                                        @Nullable val potionEffect = potionEffectData.createPotionEffect();

                                                        if (potionEffect != null)
                                                            potionEffects.add(potionEffect);
                                                    }
                                            )
                            );

                }
        );

        return potionEffects;
    }

    public static @NotNull List<PotionEffect> getAttackerEffects(@NonNull final ItemStack itemstack, @Nullable final EquipmentSlot equipmentSlot) {
        @NonNull val components = ItemUtil.getComponents(itemstack);

        if (components.isEmpty()) return List.of();

        @NonNull val potionEffects = new ArrayList<PotionEffect>();

        @NonNull val itemEffects = SlotsEffect.getInstance()
                .getConfigurationModule()
                .getItemsEffects();

        components.forEach(
                component -> {
                    itemEffects.get(component)
                            .stream()
                            .filter(
                                    effectData -> {
                                        if (equipmentSlot == null)
                                            return true;

                                        @NonNull val slots = effectData.getSlots();

                                        if (slots.contains("ALL") || slots.contains("*"))
                                            return true;

                                        return slots.contains(equipmentSlot.name());
                                    }
                            )
                            .map(
                                    (Function<EffectData, List<PotionEffect>>) potionEffectData -> potionEffectData.getAttackerEffects()
                                            .stream()
                                            .map(PotionEffectData::createPotionEffect)
                                            .toList()
                            )
                            .forEach(potionEffects::addAll);
                }
        );

        return potionEffects;
    }

    public static @NotNull List<PotionEffect> getVictimEffects(@NonNull final ItemStack itemstack) {
        @NonNull val components = ItemUtil.getComponents(itemstack);

        if (components.isEmpty()) return List.of();

        @NonNull val potionEffects = new ArrayList<PotionEffect>();

        @NonNull val itemEffects = SlotsEffect.getInstance()
                .getConfigurationModule()
                .getItemsEffects();

        components.forEach(
                component -> {
                    itemEffects.get(component)
                            .stream()
                            .map(
                                    (Function<EffectData, List<PotionEffect>>) potionEffectData -> potionEffectData.getVictimEffects()
                                            .stream()
                                            .map(PotionEffectData::createPotionEffect)
                                            .toList()
                            )
                            .forEach(potionEffects::addAll);
                }
        );

        return potionEffects;
    }
}