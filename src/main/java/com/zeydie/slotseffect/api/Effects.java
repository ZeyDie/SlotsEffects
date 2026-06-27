package com.zeydie.slotseffect.api;

import lombok.NonNull;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public final class Effects {
    public static void applyEffect(@NonNull final Player player, @NonNull final PotionEffect potionEffect) {
        player.addPotionEffect(potionEffect, true);
    }

    public static void removeEffect(@NonNull final Player player, @NonNull final PotionEffect potionEffect) {
        removeEffect(player, potionEffect.getType());
    }

    public static void removeEffect(@NonNull final Player player, @NonNull final PotionEffectType potionEffectType) {
        player.removePotionEffect(potionEffectType);
    }
}