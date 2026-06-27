package com.zeydie.slotseffect.api;

import com.zeydie.slotseffect.bukkit.data.objects.ActiveEffectSlot;
import com.zeydie.slotseffect.bukkit.utils.BukkitUtil;
import com.zeydie.slotseffect.bukkit.utils.ItemUtil;
import com.zeydie.slotseffect.mountcore.SlotsEffect;
import lombok.NonNull;
import lombok.val;
import org.bukkit.Bukkit;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public final class ItemEffects {
    private static @NotNull Map<UUID, List<ActiveEffectSlot>> activeEffectSlots = new HashMap<>();

    public static void cleanup() {
        activeEffectSlots.keySet().removeIf(uuid -> !Bukkit.getOfflinePlayer(uuid).isOnline());
    }

    public static void cleanup(@NonNull final Player player) {
        cleanup(player.getUniqueId());
    }

    public static void cleanup(@NonNull final UUID playerUniqueId) {
        activeEffectSlots.keySet().remove(playerUniqueId);
    }

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
        @NonNull val components = ItemUtil.getComponents(itemstack);

        if (components.isEmpty())
            return List.of();

        @NonNull val potionEffects = new ArrayList<PotionEffect>();

        @NonNull val itemEffects = SlotsEffect.getInstance()
                .getConfigurationModule()
                .getItemsEffects();

        for (@NonNull val component : components) {
            @Nullable val effects = itemEffects.get(component);

            if (effects == null)
                continue;

            for (@NonNull val effectData : effects) {
                @Nullable val slots = effectData.getSlots();
                
                if (slots.contains(EquipmentSlot.HAND.name()) || slots.contains(EquipmentSlot.OFF_HAND.name())) {
                    if (player.getInventory().getHeldItemSlot() != slot)
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
        }

        return potionEffects;
    }

    private static @NotNull List<PotionEffect> getAttackerEffects(@NonNull final ItemStack itemstack, @Nullable final EquipmentSlot equipmentSlot) {
        @NonNull val components = ItemUtil.getComponents(itemstack);

        if (components.isEmpty())
            return List.of();

        @NonNull val potionEffects = new ArrayList<PotionEffect>();

        @NonNull val itemEffects = SlotsEffect.getInstance()
                .getConfigurationModule()
                .getItemsEffects();

        for (@NonNull val component : components) {
            @Nullable val effects = itemEffects.get(component);

            if (effects == null)
                continue;

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
        }

        return potionEffects;
    }

    private static @NotNull List<PotionEffect> getVictimEffects(@NonNull final ItemStack itemstack) {
        @NonNull val components = ItemUtil.getComponents(itemstack);

        if (components.isEmpty())
            return List.of();

        @NonNull val potionEffects = new ArrayList<PotionEffect>();

        @NonNull val itemEffects = SlotsEffect.getInstance()
                .getConfigurationModule()
                .getItemsEffects();

        for (@NonNull val component : components) {
            @Nullable val effects = itemEffects.get(component);

            if (effects == null)
                continue;

            for (@NonNull val effectData : effects) {
                for (@NonNull val data : effectData.getVictimEffects()) {
                    @NonNull val effect = data.createPotionEffect();

                    if (effect != null && BukkitUtil.isGoodRandom(data.chance()))
                        potionEffects.add(effect);
                }
            }
        }

        return potionEffects;
    }
}