package com.zeydie.slotseffect.bukkit.utils;

import io.papermc.paper.persistence.PersistentDataContainerView;
import lombok.NonNull;
import lombok.val;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
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

    public static @NotNull Map<NamespacedKey, Integer> getArmorComponents(@NotNull final Player player) {
        @NonNull val components = new HashMap<NamespacedKey, Integer>();

        @NonNull val armor = player.getInventory().getArmorContents();

        for (@Nullable val item : armor)
            addComponents(item, components);

        return components;
    }

    private static void addComponents(@NonNull final ItemStack item, @NonNull final Map<NamespacedKey, Integer> map) {
        if (item == null)
            return;

        for (@NonNull val component : getComponents(item))
            map.merge(component, 1, Integer::sum);
    }
}