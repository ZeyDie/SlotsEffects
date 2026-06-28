package com.zeydie.slotseffect.bukkit.service;

import com.zeydie.slotseffect.bukkit.collectors.EffectCollector;
import com.zeydie.slotseffect.bukkit.collectors.EffectSynchronizer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Iterator;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public final class InventoryEffectService {

    private static final Set<UUID> dirtyPlayers = ConcurrentHashMap.newKeySet();

    public static void markDirty(Player player) {
        dirtyPlayers.add(player.getUniqueId());
    }

    public static void update(Player player) {
        EffectSynchronizer.synchronize(
                player,
                EffectCollector.collect(player)
        );
    }

    public static void flush() {
        Iterator<UUID> iterator = dirtyPlayers.iterator();

        while (iterator.hasNext()) {
            UUID uuid = iterator.next();
            iterator.remove();

            Player player = Bukkit.getPlayer(uuid);

            if (player == null)
                continue;

            update(player);
        }
    }
}