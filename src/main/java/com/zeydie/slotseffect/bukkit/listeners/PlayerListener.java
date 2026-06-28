package com.zeydie.slotseffect.bukkit.listeners;

import com.destroystokyo.paper.event.player.PlayerArmorChangeEvent;
import com.zeydie.slotseffect.bukkit.collectors.EffectSynchronizer;
import com.zeydie.slotseffect.bukkit.service.InventoryEffectService;
import lombok.NonNull;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.player.*;

public class PlayerListener implements Listener {
    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerQuit(@NonNull final PlayerQuitEvent event) {
        EffectSynchronizer.clearPlayerEffects(event.getPlayer());
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onJoin(@NonNull final PlayerJoinEvent event) {
        InventoryEffectService.markDirty(event.getPlayer());
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onInventoryClick(@NonNull final InventoryClickEvent event) {
        if (event.getWhoClicked() instanceof final Player player)
            InventoryEffectService.markDirty(player);
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onInventoryDrag(@NonNull final InventoryDragEvent event) {
        if (event.getWhoClicked() instanceof final Player player)
            InventoryEffectService.markDirty(player);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onHeld(@NonNull final PlayerItemHeldEvent event) {
        InventoryEffectService.markDirty(event.getPlayer());
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onSwap(@NonNull final PlayerSwapHandItemsEvent event) {
        InventoryEffectService.markDirty(event.getPlayer());
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onDrop(@NonNull final PlayerDropItemEvent event) {
        InventoryEffectService.markDirty(event.getPlayer());
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onPickup(@NonNull final EntityPickupItemEvent event) {
        if (event.getEntity() instanceof final Player player)
            InventoryEffectService.markDirty(player);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onRespawn(@NonNull final PlayerRespawnEvent event) {
        InventoryEffectService.markDirty(event.getPlayer());
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onDeath(@NonNull final PlayerDeathEvent event) {
        InventoryEffectService.markDirty(event.getEntity());
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onArmor(@NonNull final PlayerArmorChangeEvent event) {
        InventoryEffectService.markDirty(event.getPlayer());
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