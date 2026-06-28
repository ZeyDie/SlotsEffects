package com.zeydie.slotseffect.bukkit.collectors;

import com.zeydie.slotseffect.api.Effects;
import lombok.NonNull;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public final class EffectSynchronizer {
    private static final Map<UUID, Set<PotionEffectType>> managedEffects = new ConcurrentHashMap<>();

    public static void synchronize(@NonNull Player player, Map<PotionEffectType, PotionEffect> desired) {
        UUID uuid = player.getUniqueId();
        Set<PotionEffectType> currentlyManaged = managedEffects
                .computeIfAbsent(uuid, k -> new HashSet<>());

        Set<PotionEffectType> newlyAppliedThisTick = new HashSet<>();

        // 1. Применяем/обновляем нужные эффекты
        for (PotionEffect wanted : desired.values()) {
            PotionEffectType type = wanted.getType();
            PotionEffect current = player.getPotionEffect(type);

            boolean shouldApply = false;

            if (current == null) {
                shouldApply = true;
            } else if (current.getAmplifier() < wanted.getAmplifier() ||
                    (current.getAmplifier() == wanted.getAmplifier() && current.getDuration() < wanted.getDuration())) {
                shouldApply = true;
            }

            if (shouldApply) {
                Effects.applyEffect(player, wanted);
                newlyAppliedThisTick.add(type);
            } else if (currentlyManaged.contains(type)) {
                newlyAppliedThisTick.add(type);
            }
        }

        // 2. Удаляем ТОЛЬКО свои эффекты, которые больше не нужны
        for (PotionEffectType type : currentlyManaged) {
            if (!newlyAppliedThisTick.contains(type)) {
                // Проверяем, есть ли эффект сейчас
                if (player.hasPotionEffect(type)) {
                    // Удаляем только если это наш эффект (мы его раньше применяли)
                    // Это решает проблему с "чужими" эффектами от других плагинов
                    Effects.removeEffect(player, type);
                }
            }
        }

        // Обновляем список управляемых эффектов
        currentlyManaged.clear();
        currentlyManaged.addAll(newlyAppliedThisTick);
    }

    public static void clearPlayerEffects(@NonNull Player player) {
        Set<PotionEffectType> managed = managedEffects.remove(player.getUniqueId());
        if (managed != null) {
            for (PotionEffectType type : managed) {
                if (player.hasPotionEffect(type)) {
                    Effects.removeEffect(player, type);
                }
            }
        }
    }

    public static void cleanup() {
        managedEffects.keySet().removeIf(uuid -> {
            Player player = org.bukkit.Bukkit.getPlayer(uuid);
            return player == null || !player.isOnline();
        });
    }
}