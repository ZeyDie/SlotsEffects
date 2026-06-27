package com.zeydie.slotseffect.bukkit.tasks;

import com.zeydie.slotseffect.api.ArmorEffects;
import com.zeydie.slotseffect.api.ItemEffects;
import com.zeydie.slotseffect.bukkit.cache.InventoryCache;
import com.zeydie.slotseffect.bukkit.collectors.EffectCollector;
import com.zeydie.slotseffect.bukkit.collectors.EffectSynchronizer;
import lombok.NonNull;
import lombok.val;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Map;

public class InventoryTask extends BukkitRunnable {
    @Override
    public void run() {
        for (@NonNull Player player : Bukkit.getOnlinePlayers())
        {
            EffectSynchronizer.synchronize(player, EffectCollector.collect(player));
        }

        /*@NonNull val inventoryCache = InventoryCache.getInstance();

        for (@NonNull val player : Bukkit.getOnlinePlayers())
            inventoryCache.cache(player);

        inventoryCache.cleanup();

        ArmorEffects.cleanup();
        ItemEffects.cleanup();*/
    }
}