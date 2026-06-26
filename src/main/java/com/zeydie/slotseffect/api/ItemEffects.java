package com.zeydie.slotseffect.api;

import com.zeydie.slotseffect.bukkit.data.objects.ActiveEffectSlot;
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
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public final class ItemEffects {
    private static @NotNull Map<UUID, List<ActiveEffectSlot>> activeEffectSlots = new HashMap<>();

    public static void applyEffect(@NonNull final Player player, @NonNull final PotionEffect potionEffect) {
        player.addPotionEffect(potionEffect, true);

        //if (potionEffect.getDuration() <= PotionEffect.INFINITE_DURATION) BukkitUtil.runTaskLater(() -> removeEffect(player, potionEffect), potionEffect.getDuration());
    }

    public static void removeEffect(@NonNull final Player player, @NonNull final PotionEffect potionEffect) {
        player.removePotionEffect(potionEffect.getType());
    }

    public static void removeEffect(@NonNull final Player player, @NonNull final PotionEffectType potionEffectType) {
        player.removePotionEffect(potionEffectType);
    }

    public static void protectInventorySlot(@NonNull final Player player, @Nullable final ItemStack itemstack, final int slot) {
        @NonNull val activeEffects = activeEffectSlots.getOrDefault(player.getUniqueId(), new ArrayList<>());

        if (activeEffects.isEmpty())
            return;

        if (itemstack == null) {
            @NonNull val optional = activeEffects.stream()
                    .filter(activeEffectSlot -> activeEffectSlot.slot().equals("SLOT_" + slot))
                    .findFirst();

            if (optional.isPresent()) {
                @NonNull val slotEffect = optional.get();

                BukkitUtil.runTaskLater(() -> removeEffect(player, slotEffect.potionEffect()));

                activeEffects.remove(slotEffect);

                SlotsEffect.getInstance().logger().info("Slot " + slot + " is protected");
            }
        }
    }

    public static void applyEffects(@NonNull final Player player, @NonNull final ItemStack itemstack, final int slot) {
        BukkitUtil.runTaskLater(
                () -> {
                    SlotsEffect.getInstance().logger().info("getStaticEffects: " + itemstack + " " + slot);

                    for (@NonNull val potionEffect : getStaticEffects(itemstack, slot)) {
                        @NonNull val effect = new ActiveEffectSlot("SLOT_" + slot, potionEffect);

                        @NonNull val activeEffects = activeEffectSlots.computeIfAbsent(player.getUniqueId(), k -> new ArrayList<>());

                        if (activeEffects.isEmpty()) {
                            activeEffects.add(effect);
                            applyEffect(player, potionEffect);
                        } else {
                            @NonNull val optional = activeEffects.stream()
                                    .filter(activeEffectSlot -> activeEffectSlot.potionEffect().getType().equals(potionEffect.getType()))
                                    .findFirst();

                            if (optional.isPresent()) {
                                @NonNull val slotEffect = optional.get();

                                if (slotEffect.potionEffect().getAmplifier() < potionEffect.getAmplifier())
                                    player.removePotionEffect(potionEffect.getType());
                            } else {
                                applyEffect(player, potionEffect);
                                SlotsEffect.getInstance().logger().info("Apply effect " + potionEffect.getType().getName());
                            }
                        }
                    }
                }
        );
    }

    public static void applyAtackerEffects(@NonNull final LivingEntity attackerLivingEntity, @NonNull final ItemStack itemstack, @NonNull final EquipmentSlot equipmentSlot) {
        BukkitUtil.runTaskLater(
                () -> {
                    if (attackerLivingEntity instanceof final Player player)
                        for (@NonNull val potionEffect : getAttackerEffects(itemstack, equipmentSlot))
                            applyEffect(player, potionEffect);
                }
        );
    }

    public static void applyVictimEffects(@NonNull final LivingEntity victimLivingEntity, @NonNull final ItemStack itemstack) {
        BukkitUtil.runTaskLater(
                () -> {
                    if (victimLivingEntity instanceof final Player player)
                        for (@NonNull val potionEffect : getVictimEffects(itemstack))
                            applyEffect(player, potionEffect);
                }
        );
    }

    private static @NotNull List<PotionEffect> getStaticEffects(@NonNull final ItemStack itemstack, final int slot) {
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

                if (
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

    public static @NotNull List<PotionEffect> getAttackerEffects(@NonNull final ItemStack itemstack, @Nullable final EquipmentSlot equipmentSlot) {
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

    public static @NotNull List<PotionEffect> getVictimEffects(@NonNull final ItemStack itemstack) {
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