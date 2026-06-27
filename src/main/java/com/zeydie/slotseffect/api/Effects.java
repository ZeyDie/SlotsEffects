package com.zeydie.slotseffect.api;

import lombok.NonNull;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

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