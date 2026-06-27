package com.zeydie.slotseffect.bukkit.collectors;

import lombok.NonNull;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public final class EffectSynchronizer {

    public static void synchronize(@NonNull Player player, Map<PotionEffectType, PotionEffect> desired) {
        // Текущие эффекты игрока
        Collection<PotionEffect> current = player.getActivePotionEffects();

        Set<PotionEffectType> toRemove = new HashSet<>();

        // Удаляем те, которых нет в desired
        for (PotionEffect active : current) {
            PotionEffectType type = active.getType();
            if (!desired.containsKey(type)) {
                toRemove.add(type);
            } else {
                PotionEffect wanted = desired.get(type);
                // Если amplifier ниже нужного — обновляем
                if (active.getAmplifier() < wanted.getAmplifier()) {
                    player.removePotionEffect(type);
                    player.addPotionEffect(wanted, true);
                }
            }
        }

        // Удаляем ненужные
        for (PotionEffectType type : toRemove) {
            player.removePotionEffect(type);
        }

        // Добавляем/обновляем нужные
        for (PotionEffect wanted : desired.values()) {
            if (!player.hasPotionEffect(wanted.getType()) ||
                    player.getPotionEffect(wanted.getType()).getAmplifier() < wanted.getAmplifier()) {
                player.addPotionEffect(wanted, true);
            }
        }
    }
}