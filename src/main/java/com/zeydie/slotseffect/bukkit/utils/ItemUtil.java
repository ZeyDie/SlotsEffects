package com.zeydie.slotseffect.bukkit.utils;

import io.papermc.paper.persistence.PersistentDataContainerView;
import lombok.NonNull;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

public final class ItemUtil {
    public static boolean hasComponent(@NonNull final ItemStack itemstack, @NonNull final NamespacedKey component) {
        return getContainer(itemstack).has(component);
    }

    public static @NotNull PersistentDataContainerView getContainer(@NonNull final ItemStack itemstack) {
        return itemstack.getPersistentDataContainer();
    }

    public static @NotNull Set<NamespacedKey> getComponents(@NonNull final ItemStack itemstack) {
        return getContainer(itemstack).getKeys();
    }
}