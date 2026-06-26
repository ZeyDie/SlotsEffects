package com.zeydie.slotseffect.bukkit.cache;

import com.zeydie.slotseffect.api.ArmorEffects;
import com.zeydie.slotseffect.api.ItemEffects;
import lombok.Getter;
import lombok.NonNull;
import lombok.val;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public final class InventoryCache {
    @Getter
    private static final @NotNull InventoryCache instance = new InventoryCache();

    private final @NotNull Map<UUID, ItemStack[]> inventoryCache = new HashMap<>();
    private final @NotNull Map<UUID, ItemStack[]> armorCache = new HashMap<>();

    public void cache(@NonNull final Player player) {
        @NonNull val playerUniqueId = player.getUniqueId();
        @NonNull val playerInventory = player.getInventory();

        @NonNull val contents = playerInventory.getContents();
        @NonNull val armorContents = playerInventory.getArmorContents();

        @NonNull val inventories = this.inventoryCache.computeIfAbsent(playerUniqueId, uuid -> new ItemStack[contents.length]);

        for (int i = 0; i < contents.length; i++) {
            @Nullable val itemStack = contents[i];
            @Nullable val cachedItemStack = inventories[i];

            if (cachedItemStack == null || !cachedItemStack.isSimilar(itemStack)) {
                inventories[i] = itemStack;

                if (itemStack != null)
                    ItemEffects.applyEffects(player, itemStack, i);
                else ItemEffects.protectInventorySlot(player, itemStack, i);
            }
        }

        @NonNull val armors = this.armorCache.computeIfAbsent(playerUniqueId, uuid -> new ItemStack[armorContents.length]);

        for (int i = 0; i < armorContents.length; i++) {
            @Nullable val itemStack = armorContents[i];
            @Nullable val cachedItemStack = armors[i];

            /*SlotsEffect.getInstance().logger().info("==========================");
            SlotsEffect.getInstance().logger().info("slot: " + i);
            SlotsEffect.getInstance().logger().info("itemStack: " + itemStack);
            SlotsEffect.getInstance().logger().info("cachedItemStack: " + cachedItemStack);*/

            if (cachedItemStack == null || !cachedItemStack.isSimilar(itemStack)) {
                armors[i] = itemStack;

                if (itemStack != null)
                    ArmorEffects.applyArmorEffects(player, itemStack, i);
                else ArmorEffects.protectArmorSlot(player, itemStack, i);
            }
        }

        ArmorEffects.applyArmorSets(player);

        /*for (int i = 0; i < inventory.getStorageContents().length; i++) {
            @Nullable val itemStack = inventory.getStorageContents()[i];

            if (items.get(i) != null && !items.get(i).equals(itemStack)) {
                items.set(i, itemStack);
                InventoryHandler.getInstance().updateStaticItem(player, itemStack);
            }
        }*/
    }

    public void uncache(@NonNull final Player player) {
        @NonNull val playerUniqueId = player.getUniqueId();

        this.inventoryCache.remove(playerUniqueId);
        this.armorCache.remove(playerUniqueId);
    }
}