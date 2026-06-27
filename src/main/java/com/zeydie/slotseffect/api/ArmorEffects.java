package com.zeydie.slotseffect.api;

import com.zeydie.slotseffect.bukkit.data.objects.ActiveEffectSlot;
import com.zeydie.slotseffect.bukkit.utils.BukkitUtil;
import com.zeydie.slotseffect.bukkit.utils.ItemUtil;
import com.zeydie.slotseffect.mountcore.SlotsEffect;
import lombok.NonNull;
import lombok.val;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public final class ArmorEffects {
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

    public static void protectArmorSlot(@NonNull final Player player, @Nullable final ItemStack itemstack, final int slot) {
        @NonNull val activeEffects = activeEffectSlots.getOrDefault(player.getUniqueId(), new ArrayList<>());

        if (activeEffects.isEmpty())
            return;

        if (itemstack == null) {
            @NonNull val armorSlot = BukkitUtil.getEquipmentOfArmorSlot(slot);

            @NonNull val list = activeEffects.stream()
                    .filter(activeEffectSlot -> activeEffectSlot.slot().equals(armorSlot))
                    .toList();

            for (@NonNull val slotEffect : list) {
                Effects.removeEffect(player, slotEffect.potionEffect());

                activeEffects.remove(slotEffect);

                SlotsEffect.getInstance().logger().debug("Slot " + slot + " (" + armorSlot + ")" + " is protected");
            }
        }
    }

    public static void applyArmorEffects(@NonNull final Player player, @NonNull final ItemStack itemstack, final int slot) {
        SlotsEffect.getInstance().logger().debug("getStaticEffects: " + itemstack + " " + slot);

        @NonNull val staticEffects = getStaticEffects(itemstack, slot);

        if (staticEffects.isEmpty())
            return;

        for (@NonNull val potionEffect : staticEffects) {
            @Nullable val armorSlot = BukkitUtil.getEquipmentOfArmorSlot(slot);

            if (armorSlot == null) {
                SlotsEffect.getInstance().logger().debug("Armor slot " + slot + " is null");
                return;
            }

            @NonNull val effect = new ActiveEffectSlot(armorSlot.name(), potionEffect);

            @NonNull val activeEffects = activeEffectSlots.computeIfAbsent(player.getUniqueId(), k -> new ArrayList<>());

            SlotsEffect.getInstance().logger().debug("==========================");
            SlotsEffect.getInstance().logger().debug("activeEffects: " + activeEffects);
            SlotsEffect.getInstance().logger().debug("armorSlot: " + armorSlot);

            @NonNull val optional = activeEffects.stream()
                    .filter(activeEffectSlot -> activeEffectSlot.potionEffect().getType().equals(potionEffect.getType()))
                    .findFirst();

            SlotsEffect.getInstance().logger().debug("optional: " + optional);

            if (optional.isPresent()) {
                @NonNull val slotEffect = optional.get();

                if (slotEffect.potionEffect().getAmplifier() < potionEffect.getAmplifier())
                    player.removePotionEffect(potionEffect.getType());

                if (player.hasPotionEffect(potionEffect.getType()))
                    activeEffects.remove(effect);
            }

            activeEffects.add(effect);
            Effects.applyEffect(player, potionEffect);
            SlotsEffect.getInstance().logger().debug("Apply effect " + BukkitUtil.getPotionName(potionEffect));
        }

        staticEffects.clear();
    }

    public static void applyHitEffects(@NonNull final Player player, @NonNull final ItemStack itemstack, final int slot) {
        SlotsEffect.getInstance().logger().debug("getHitEffects: " + itemstack + " " + slot);

        @NonNull val hitEffects = getHitEffects(itemstack, slot);

        if (hitEffects.isEmpty())
            return;

        for (@NonNull val potionEffect : hitEffects) {
            @Nullable val armorSlot = BukkitUtil.getEquipmentOfArmorSlot(slot);

            if (armorSlot == null) {
                SlotsEffect.getInstance().logger().debug("Armor slot " + slot + " is null");
                return;
            }

            @NonNull val effect = new ActiveEffectSlot(armorSlot.name(), potionEffect);

            @NonNull val activeEffects = activeEffectSlots.computeIfAbsent(player.getUniqueId(), k -> new ArrayList<>());

            SlotsEffect.getInstance().logger().debug("==========================");
            SlotsEffect.getInstance().logger().debug("activeEffects: " + activeEffects);
            SlotsEffect.getInstance().logger().debug("armorSlot: " + armorSlot);

            @NonNull val list = activeEffects.stream()
                    .filter(activeEffectSlot -> activeEffectSlot.potionEffect().getType().equals(potionEffect.getType()))
                    .toList();

            for (@NonNull val slotEffect : list) {
                if (slotEffect.potionEffect().getAmplifier() < potionEffect.getAmplifier())
                    player.removePotionEffect(potionEffect.getType());

                if (player.hasPotionEffect(potionEffect.getType()))
                    activeEffects.remove(effect);
            }

            activeEffects.add(effect);
            Effects.applyEffect(player, potionEffect);
            SlotsEffect.getInstance().logger().debug("Apply effect " + BukkitUtil.getPotionName(potionEffect));
        }

        hitEffects.clear();
    }

    public static void applyArmorSets(@NotNull final Player player) {
        @NonNull val armorSetEffects = SlotsEffect.getInstance()
                .getConfigurationModule()
                .getArmorSetsEffects();


    }

    public static @NotNull List<PotionEffect> getStaticEffects(@NonNull final ItemStack itemstack, final int slot) {
        @NonNull val components = ItemUtil.getComponents(itemstack);

        if (components.isEmpty())
            return List.of();

        @NonNull val potionEffects = new ArrayList<PotionEffect>();

        @NonNull val itemEffects = SlotsEffect.getInstance()
                .getConfigurationModule()
                .getArmorEffects();

        for (@NonNull val component : components) {
            @Nullable val effects = itemEffects.get(component);

            if (effects == null)
                continue;

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
        }

        return potionEffects;
    }

    private static @NotNull List<PotionEffect> getHitEffects(@NonNull final ItemStack itemstack, final int slot) {
        @NonNull val components = ItemUtil.getComponents(itemstack);

        if (components.isEmpty())
            return List.of();

        @NonNull val potionEffects = new ArrayList<PotionEffect>();

        @NonNull val itemEffects = SlotsEffect.getInstance()
                .getConfigurationModule()
                .getArmorEffects();

        for (@NonNull val component : components) {
            @Nullable val effects = itemEffects.get(component);

            if (effects == null)
                continue;

            for (@NonNull val effectData : effects) {
                @Nullable val equipmentSlot = effectData.getEquipmentSlot();

                if (equipmentSlot != null && equipmentSlot != BukkitUtil.getEquipmentOfArmorSlot(slot))
                    continue;

                for (@NonNull val data : effectData.getHitEffects()) {
                    @NonNull val effect = data.createPotionEffect();

                    if (effect != null && BukkitUtil.isGoodRandom(data.chance()))
                        potionEffects.add(effect);
                }
            }
        }

        return potionEffects;
    }
}