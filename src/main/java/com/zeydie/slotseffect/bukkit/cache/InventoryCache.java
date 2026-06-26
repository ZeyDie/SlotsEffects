package com.zeydie.slotseffect.bukkit.cache;

import com.zeydie.slotseffect.api.ItemEffects;
import lombok.Getter;
import lombok.NonNull;
import lombok.val;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public final class InventoryCache {
    @Getter
    private static final @NotNull InventoryCache instance = new InventoryCache();

    private final @NotNull Map<UUID, List<ItemStack>> cache = new HashMap<>();

    public void cache(@NonNull final Player player) {
        @NonNull val items = this.cache.getOrDefault(player.getUniqueId(), new LinkedList<>());

        @NonNull val inventory = player.getInventory();

        for (int i = 0; i < inventory.getContents().length; i++) {
            @Nullable val itemStack = inventory.getContents()[i];

            if (items.get(i) != null && !items.get(i).equals(itemStack)) {
                items.set(i, itemStack);
                ItemEffects.applyEffects(player, itemStack, i);
            }
        }

        for (int i = 0; i < inventory.getArmorContents().length; i++) {
            @Nullable val itemStack = inventory.getArmorContents()[i];

            if (items.get(i) != null && !items.get(i).equals(itemStack)) {
                items.set(i, itemStack);
                ItemEffects.applyEffects(player, itemStack, i);
            }
        }

        /*for (int i = 0; i < inventory.getStorageContents().length; i++) {
            @Nullable val itemStack = inventory.getStorageContents()[i];

            if (items.get(i) != null && !items.get(i).equals(itemStack)) {
                items.set(i, itemStack);
                InventoryHandler.getInstance().updateStaticItem(player, itemStack);
            }
        }*/
    }
}