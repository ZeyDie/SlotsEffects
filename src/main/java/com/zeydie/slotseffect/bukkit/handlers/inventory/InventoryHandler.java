package com.zeydie.slotseffect.bukkit.handlers.inventory;

import com.zeydie.slotseffect.api.ItemEffects;
import lombok.Getter;
import lombok.NonNull;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public final class InventoryHandler {
    @Getter
    private static final @NotNull InventoryHandler instance = new InventoryHandler();

    public void updateStaticItem(
            @NonNull final Player player,
            @NonNull final ItemStack itemStack
    ) {
        ItemEffects.getStaticEffects(itemStack).forEach(effect -> player.addPotionEffect(effect));
    }
}