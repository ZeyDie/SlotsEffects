package com.zeydie.slotseffect.bukkit.collectors;

import com.zeydie.slotseffect.api.Effects;
import lombok.NonNull;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public final class EffectSynchronizer {
    // Храним последний эффект, который применил именно SlotsEffect
    private static final Map<UUID, Map<PotionEffectType, PotionEffect>> managedEffects = new ConcurrentHashMap<>();

    public static void synchronize(@NonNull Player player, Map<PotionEffectType, PotionEffect> desired) {
        UUID uuid = player.getUniqueId();
        Map<PotionEffectType, PotionEffect> currentlyManaged = managedEffects
                .computeIfAbsent(uuid, k -> new HashMap<>());

        Map<PotionEffectType, PotionEffect> newlyAppliedThisTick = new HashMap<>();

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
                newlyAppliedThisTick.put(type, wanted);
            } else if (currentlyManaged.containsKey(type)) {
                newlyAppliedThisTick.put(type, wanted); // обновляем наш "отпечаток"
            }
        }

        // Удаляем ТОЛЬКО те эффекты, которые мы сами выдали и которые больше не нужны
        for (Map.Entry<PotionEffectType, PotionEffect> entry : currentlyManaged.entrySet()) {
            PotionEffectType type = entry.getKey();
            if (!newlyAppliedThisTick.containsKey(type)) {
                PotionEffect current = player.getPotionEffect(type);

                if (current != null) {
                    PotionEffect ourLastEffect = entry.getValue();

                    // Не удаляем, если текущий эффект СИЛЬНЕЕ или ДЛИННЕЕ нашего последнего
                    if (current.getAmplifier() > ourLastEffect.getAmplifier() ||
                            (current.getAmplifier() == ourLastEffect.getAmplifier() &&
                                    current.getDuration() > ourLastEffect.getDuration())) {
                        continue; // чужой/лучший эффект — не трогаем
                    }

                    Effects.removeEffect(player, type);
                }
            }
        }

        // Обновляем managed
        currentlyManaged.clear();
        currentlyManaged.putAll(newlyAppliedThisTick);
    }

    public static void clearPlayerEffects(@NonNull Player player) {
        Map<PotionEffectType, PotionEffect> managed = managedEffects.remove(player.getUniqueId());
        if (managed != null) {
            for (PotionEffectType type : managed.keySet()) {
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