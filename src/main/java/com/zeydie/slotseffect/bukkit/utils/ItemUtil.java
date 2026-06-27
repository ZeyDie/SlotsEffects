package com.zeydie.slotseffect.bukkit.utils;

import lombok.NonNull;
import lombok.val;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class ItemUtil {
    private static final Pattern COMPONENT_PATTERN = Pattern.compile(
            "key:\\s*\"([^\"]+)\"\\s*,\\s*namespace:\\s*\"([^\"]+)\"",
            Pattern.CASE_INSENSITIVE
    );

    public static boolean hasComponent(@NonNull final ItemStack itemstack, @NonNull final NamespacedKey component) {
        if (itemstack == null || !itemstack.hasItemMeta()) return false;

        String componentsStr = itemstack.getItemMeta().getAsComponentString();
        if (componentsStr == null || componentsStr.isEmpty()) return false;

        String targetNamespace = component.getNamespace();
        String targetKey = component.getKey();

        Matcher matcher = COMPONENT_PATTERN.matcher(componentsStr);
        while (matcher.find()) {
            String key = matcher.group(1);
            String namespace = matcher.group(2);

            if (targetKey.equals(key) && targetNamespace.equals(namespace)) {
                return true;
            }
        }

        return false;
    }

    public static @NotNull Set<NamespacedKey> getComponents(@NonNull final ItemStack itemstack) {
        Set<NamespacedKey> keys = new HashSet<>();

        if (itemstack == null || !itemstack.hasItemMeta()) return keys;

        String componentString = itemstack.getItemMeta().getAsComponentString();
        if (componentString == null) return keys;


        Matcher matcher = COMPONENT_PATTERN.matcher(componentString);

        while (matcher.find()) {
            String key = matcher.group(1);
            String namespace = matcher.group(2);

            keys.add(new NamespacedKey(namespace, key));
        }

        return keys;
    }

    public static @NotNull Map<NamespacedKey, Integer> getArmorComponents(@NotNull final Player player) {
        @NonNull val components = new java.util.HashMap<NamespacedKey, Integer>();

        @NonNull val armor = player.getInventory().getArmorContents();

        for (@Nullable val item : armor)
            addComponents(item, components);

        return components;
    }

    private static void addComponents(@Nullable final ItemStack item, @NonNull final Map<NamespacedKey, Integer> map) {
        if (item == null) return;

        for (@NonNull val component : getComponents(item))
            map.merge(component, 1, Integer::sum);
    }
}