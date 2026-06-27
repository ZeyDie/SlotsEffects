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
    public static @Nullable NamespacedKey getComponent(@NonNull final ItemStack itemstack) {
        @Nullable val itemMeta = itemstack.getItemMeta();

        if (itemMeta == null)
            return null;

        return itemMeta.getItemModel();
    }

    public static boolean hasComponent(@NonNull final ItemStack itemstack, @NonNull final NamespacedKey component) {
        return getComponent(itemstack).equals(component);
    }

    public static @NotNull Map<NamespacedKey, Integer> getArmorComponents(@NotNull final Player player) {
        @NonNull val components = new HashMap<NamespacedKey, Integer>();

        @NonNull val armor = player.getInventory().getArmorContents();

        for (@Nullable val item : armor)
            addComponents(item, components);

        return components;
    }

    private static void addComponents(@Nullable final ItemStack item, @NonNull final Map<NamespacedKey, Integer> map) {
        if (item == null)
            return;

        map.merge(getComponent(item), 1, Integer::sum);
    }
}