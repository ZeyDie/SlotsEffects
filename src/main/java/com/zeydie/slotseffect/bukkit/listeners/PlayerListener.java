package com.zeydie.slotseffect.bukkit.listeners;

import com.zeydie.slotseffect.bukkit.cache.InventoryCache;
import lombok.NonNull;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerListener implements Listener {
    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerJoin(@NonNull final PlayerJoinEvent event) {
        InventoryCache.getInstance().cache(event.getPlayer());
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerQuit(@NonNull final PlayerQuitEvent event) {
        InventoryCache.getInstance().uncache(event.getPlayer());
    }
}
