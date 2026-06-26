package com.zeydie.slotseffect.bukkit.tasks;

import com.zeydie.slotseffect.bukkit.cache.InventoryCache;
import com.zeydie.slotseffect.bukkit.utils.BukkitUtil;
import lombok.NonNull;
import lombok.val;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

public class InventoryTask extends BukkitRunnable {
    @Override
    public void run() {
        for (@NonNull val player : Bukkit.getOnlinePlayers())
            BukkitUtil.runTaskAsynchronously( () -> InventoryCache.getInstance().cache(player));
    }
}