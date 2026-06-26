package com.zeydie.slotseffect.api;

import com.zeydie.slotseffect.bukkit.data.objects.ActiveEffectSlot;
import com.zeydie.slotseffect.bukkit.utils.BukkitUtil;
import com.zeydie.slotseffect.bukkit.utils.ItemUtil;
import com.zeydie.slotseffect.mountcore.SlotsEffect;
import lombok.NonNull;
import lombok.val;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public final class ArmorEffects {
    private static @NotNull Map<UUID, List<ActiveEffectSlot>> activeEffectSlots = new HashMap<>();

    public static void protectArmorSlot(@NonNull final Player player, @Nullable final ItemStack itemstack, final int slot) {
        @NonNull val activeEffects = activeEffectSlots.getOrDefault(player.getUniqueId(), new ArrayList<>());

        if (activeEffects.isEmpty())
            return;

        if (itemstack == null) {
            @NonNull val armorSlot = BukkitUtil.getEquipmentOfArmorSlot(slot);

            @NonNull val optional = activeEffects.stream()
                    .filter(activeEffectSlot -> activeEffectSlot.slot().equals(armorSlot))
                    .findFirst();

            if (optional.isPresent()) {
                @NonNull val slotEffect = optional.get();

                BukkitUtil.runTaskLater(() -> ItemEffects.removeEffect(player, slotEffect.potionEffect()));

                activeEffects.remove(slotEffect);

                SlotsEffect.getInstance().logger().info("Slot " + slot + " (" + armorSlot + ")" + " is protected");
            }
        }
    }

    public static void applyArmorEffects(@NonNull final Player player, @NonNull final ItemStack itemstack, final int slot) {
        BukkitUtil.runTaskLater(
                () -> {
                    SlotsEffect.getInstance().logger().info("getStaticEffects: " + itemstack + " " + slot);

                    for (@NonNull val potionEffect : getStaticEffects(itemstack, slot)) {
                        @Nullable val armorSlot = BukkitUtil.getEquipmentOfArmorSlot(slot);

                        if (armorSlot == null) {
                            SlotsEffect.getInstance().logger().info("Armor slot " + slot + " is null");
                            return;
                        }

                        @NonNull val effect = new ActiveEffectSlot(armorSlot.name(), potionEffect);

                        @NonNull val activeEffects = activeEffectSlots.computeIfAbsent(player.getUniqueId(), k -> new ArrayList<>());

                        SlotsEffect.getInstance().logger().info("==========================");
                        SlotsEffect.getInstance().logger().info("activeEffects: " + activeEffects);
                        SlotsEffect.getInstance().logger().info("armorSlot: " + armorSlot);

                        @NonNull val optional = activeEffects.stream()
                                .filter(activeEffectSlot -> activeEffectSlot.potionEffect().getType().equals(potionEffect.getType()))
                                .findFirst();

                        SlotsEffect.getInstance().logger().info("optional: " + optional);

                        if (optional.isPresent()) {
                            @NonNull val slotEffect = optional.get();

                            if (slotEffect.potionEffect().getAmplifier() < potionEffect.getAmplifier())
                                player.removePotionEffect(potionEffect.getType());

                            if (!player.hasPotionEffect(potionEffect.getType()))
                                activeEffects.remove(effect);
                        }

                        activeEffects.add(effect);
                        ItemEffects.applyEffect(player, potionEffect);
                        SlotsEffect.getInstance().logger().info("Apply effect " + potionEffect.getType().getName());
                    }
                }
        );
    }

    public static void applyHitEffects(@NonNull final Player player, @NonNull final ItemStack itemstack, final int slot) {
        BukkitUtil.runTaskLater(
                () -> {
                    SlotsEffect.getInstance().logger().info("getHitEffects: " + itemstack + " " + slot);

                    for (@NonNull val potionEffect : getHitEffects(itemstack, slot)) {
                        @Nullable val armorSlot = BukkitUtil.getEquipmentOfArmorSlot(slot);

                        if (armorSlot == null) {
                            SlotsEffect.getInstance().logger().info("Armor slot " + slot + " is null");
                            return;
                        }

                        @NonNull val effect = new ActiveEffectSlot(armorSlot.name(), potionEffect);

                        @NonNull val activeEffects = activeEffectSlots.computeIfAbsent(player.getUniqueId(), k -> new ArrayList<>());

                        SlotsEffect.getInstance().logger().info("==========================");
                        SlotsEffect.getInstance().logger().info("activeEffects: " + activeEffects);
                        SlotsEffect.getInstance().logger().info("armorSlot: " + armorSlot);

                        @NonNull val optional = activeEffects.stream()
                                .filter(activeEffectSlot -> activeEffectSlot.potionEffect().getType().equals(potionEffect.getType()))
                                .findFirst();

                        SlotsEffect.getInstance().logger().info("optional: " + optional);

                        if (optional.isPresent()) {
                            @NonNull val slotEffect = optional.get();

                            if (slotEffect.potionEffect().getAmplifier() < potionEffect.getAmplifier())
                                player.removePotionEffect(potionEffect.getType());

                            if (!player.hasPotionEffect(potionEffect.getType()))
                                activeEffects.remove(effect);
                        }

                        activeEffects.add(effect);
                        ItemEffects.applyEffect(player, potionEffect);
                        SlotsEffect.getInstance().logger().info("Apply effect " + potionEffect.getType().getName());
                    }
                }
        );
    }

    public static void applyArmorSets(@NotNull final Player player) {
        @NonNull val armorSetEffects = SlotsEffect.getInstance()
                .getConfigurationModule()
                .getArmorSetsEffects();


    }

    private static @NotNull List<PotionEffect> getStaticEffects(@NonNull final ItemStack itemstack, final int slot) {
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