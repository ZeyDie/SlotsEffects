package com.zeydie.slotseffect.bukkit.utils;

import com.zeydie.slotseffect.mountcore.SlotsEffect;
import lombok.NonNull;
import org.bukkit.Bukkit;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;

public final class BukkitUtil {
    public static @NotNull List<PotionEffectType> getEffects() {
        return Arrays.asList(PotionEffectType.values());
    }

    public static void runTaskLater(@NonNull final Runnable runnable) {
        Bukkit.getScheduler().runTaskLater(SlotsEffect.getInstance(), runnable, 1);
    }
}