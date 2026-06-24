package com.zeydie.slotseffect.bukkit.tasks;

import com.zeydie.slotseffect.bukkit.cache.InventoryCache;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

@RequiredArgsConstructor
public class InventoryTask extends BukkitRunnable {
    private final @NotNull Player player;

    @Override
    public void run() {
        InventoryCache.getInstance().cache(this.player);
    }
}