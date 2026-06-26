package com.zeydie.slotseffect.bukkit.utils;

import com.zeydie.slotseffect.mountcore.SlotsEffect;
import lombok.Getter;
import lombok.NonNull;
import org.bukkit.Bukkit;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public final class BukkitUtil {
    @Getter
    private static final @NotNull List<PotionEffectType> effects = List.of(PotionEffectType.values());

    public static void runTaskLater(@NonNull final Runnable runnable) {
        Bukkit.getScheduler().runTaskLater(SlotsEffect.getInstance(), runnable, 1);
    }

    public static void runTaskAsynchronously(@NonNull final Runnable runnable) {
        Bukkit.getScheduler().runTaskAsynchronously(SlotsEffect.getInstance(), runnable);
    }
}