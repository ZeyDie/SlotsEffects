package com.zeydie.slotseffect.bukkit.tasks;

import com.zeydie.slotseffect.bukkit.service.InventoryEffectService;
import org.bukkit.scheduler.BukkitRunnable;

public class InventoryTask extends BukkitRunnable {
    @Override
    public void run() {
        InventoryEffectService.flush();
        /*for (@NonNull Player player : Bukkit.getOnlinePlayers())
            EffectSynchronizer.synchronize(player, EffectCollector.collect(player));*/
    }
}