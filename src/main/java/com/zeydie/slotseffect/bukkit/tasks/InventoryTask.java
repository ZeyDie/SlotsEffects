package com.zeydie.slotseffect.bukkit.tasks;

import com.zeydie.slotseffect.bukkit.collectors.EffectCollector;
import com.zeydie.slotseffect.bukkit.collectors.EffectSynchronizer;
import lombok.NonNull;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class InventoryTask extends BukkitRunnable {
    @Override
    public void run() {
        for (@NonNull Player player : Bukkit.getOnlinePlayers())
            EffectSynchronizer.synchronize(player, EffectCollector.collect(player));
    }
}