package com.zeydie.slotseffect.bukkit.listeners;

import com.zeydie.slotseffect.bukkit.collectors.EffectSynchronizer;
import lombok.NonNull;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerListener implements Listener {
    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerQuit(@NonNull final PlayerQuitEvent event) {
        EffectSynchronizer.clearPlayerEffects(event.getPlayer());
    }

    /*@EventHandler(priority = EventPriority.MONITOR)
    public void test(@NonNull final PlayerJoinEvent event) {
        @NonNull val player = event.getPlayer();

        @NonNull val itemStack = new ItemStack(Material.DIAMOND, 1);
        @NonNull val head = new ItemStack(Material.CHAINMAIL_HELMET, 1);
        @NonNull val chestplate = new ItemStack(Material.DIAMOND_CHESTPLATE, 1);

        itemStack.editPersistentDataContainer(persistentDataContainer -> persistentDataContainer.set(new NamespacedKey("namespace", "diamond"), PersistentDataType.STRING, "1"));
        head.editPersistentDataContainer(persistentDataContainer -> persistentDataContainer.set(new NamespacedKey("namespace", "helmet"), PersistentDataType.STRING, "1"));
        chestplate.editPersistentDataContainer(persistentDataContainer -> persistentDataContainer.set(new NamespacedKey("namespace", "chestplate"), PersistentDataType.STRING, "1"));

        player.getInventory().addItem(itemStack, head, chestplate);
        player.updateInventory();
    }*/
}