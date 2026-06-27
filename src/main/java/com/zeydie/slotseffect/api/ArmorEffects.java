package com.zeydie.slotseffect.api;

import com.zeydie.slotseffect.bukkit.utils.BukkitUtil;
import com.zeydie.slotseffect.bukkit.utils.ItemUtil;
import com.zeydie.slotseffect.mountcore.SlotsEffect;
import com.zeydie.slotseffect.mountcore.utils.MountUtil;
import lombok.NonNull;
import lombok.val;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public final class ArmorEffects {
    public static void applyHitEffects(@NonNull final Player player, @NonNull final ItemStack itemstack, final int slot) {
        MountUtil.getLogger().debug("getHitEffects: " + itemstack + " " + slot);

        @NonNull val hitEffects = getHitEffects(itemstack, slot);

        if (hitEffects.isEmpty())
            return;

        for (@NonNull val potionEffect : hitEffects) {
            @Nullable val armorSlot = BukkitUtil.getEquipmentOfArmorSlot(slot);

            if (armorSlot == null) {
                MountUtil.getLogger().debug("Armor slot " + slot + " is null");
                return;
            }

            Effects.applyEffect(player, potionEffect);
            MountUtil.getLogger().debug("Apply effect " + BukkitUtil.getPotionName(potionEffect));
        }

        hitEffects.clear();
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