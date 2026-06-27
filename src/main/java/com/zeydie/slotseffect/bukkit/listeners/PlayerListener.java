package com.zeydie.slotseffect.bukkit.listeners;

import com.zeydie.slotseffect.api.ArmorEffects;
import com.zeydie.slotseffect.api.ItemEffects;
import lombok.NonNull;
import lombok.val;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

public class PlayerListener implements Listener {
    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerQuit(@NonNull final PlayerQuitEvent event) {
        @NonNull val player = event.getPlayer();

        ArmorEffects.cleanup(player);
        ItemEffects.cleanup(player);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void test(@NonNull final PlayerJoinEvent event) {
        @NonNull val player = event.getPlayer();

        @NonNull val itemStack = new ItemStack(Material.DIAMOND, 1);
        @NonNull val head = new ItemStack(Material.CHAINMAIL_HELMET, 1);

        itemStack.editPersistentDataContainer(persistentDataContainer -> persistentDataContainer.set(new NamespacedKey("namespace", "diamond"), PersistentDataType.STRING, ""));
        head.editPersistentDataContainer(persistentDataContainer -> persistentDataContainer.set(new NamespacedKey("namespace", "helmet"), PersistentDataType.STRING, ""));

        player.getInventory().addItem(itemStack, head);
        player.updateInventory();
    }
}