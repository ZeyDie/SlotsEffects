package com.zeydie.slotseffect.bukkit.utils;

import com.zeydie.slotseffect.mountcore.SlotsEffect;
import lombok.Getter;
import lombok.NonNull;
import org.bukkit.Bukkit;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Random;

public final class BukkitUtil {
    private static final @NotNull Random random = new Random();

    @Getter
    private static final @NotNull List<PotionEffectType> effects = List.of(PotionEffectType.values());

    public static boolean isGoodRandom(final double chance) {
        return random.nextDouble() < chance;
    }

    public static void runTaskLater(@NonNull final Runnable runnable) {
        runTaskLater(runnable, 1);
    }

    public static void runTaskLater(@NonNull final Runnable runnable, final long delay) {
        Bukkit.getScheduler().runTaskLater(SlotsEffect.getInstance(), runnable, delay);
    }

    public static void runTaskAsynchronously(@NonNull final Runnable runnable) {
        Bukkit.getScheduler().runTaskAsynchronously(SlotsEffect.getInstance(), runnable);
    }

    public static @Nullable EquipmentSlot getEquipmentOfArmorSlot(final int slot) {
        switch (slot) {
            case 4 -> {
                return EquipmentSlot.OFF_HAND;
            }
            case 3 -> {
                return EquipmentSlot.HEAD;
            }
            case 2 -> {
                return EquipmentSlot.CHEST;
            }
            case 1 -> {
                return EquipmentSlot.LEGS;
            }
            case 0 -> {
                return EquipmentSlot.FEET;
            }
        }

        return null;
    }
}