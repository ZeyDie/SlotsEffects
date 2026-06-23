package com.zeydie.slotseffect.bukkit.utils;

import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;

public final class BukkitUtil {
    public static @NotNull List<PotionEffectType> getEffects() {
        return Arrays.asList(PotionEffectType.values());
    }
}