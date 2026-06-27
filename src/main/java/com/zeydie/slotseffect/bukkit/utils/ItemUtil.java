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
    // Паттерн для поиска компонентов в формате {key: "...", namespace: "..."}
    private static final Pattern COMPONENT_PATTERN = Pattern.compile(
            "key:\\s*\"([^\"]+)\"\\s*,\\s*namespace:\\s*\"([^\"]+)\"",
            Pattern.CASE_INSENSITIVE
    );

    /**
     * Проверяет наличие компонента
     */
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

    /**
     * Возвращает все компоненты
     */
    public static @NotNull Set<NamespacedKey> getComponents(@NonNull final ItemStack itemstack) {
        Set<NamespacedKey> keys = new HashSet<>();

        if (itemstack == null || !itemstack.hasItemMeta()) return keys;

        String componentString = itemstack.getItemMeta().getAsComponentString();
        if (componentString == null) return keys;

        //System.out.println("componentString: " + componentString); // для отладки

        Matcher matcher = COMPONENT_PATTERN.matcher(componentString);

        while (matcher.find()) {
            String key = matcher.group(1);
            String namespace = matcher.group(2);

            keys.add(new NamespacedKey(namespace, key));
            //System.out.println("Found component: " + namespace + ":" + key);
        }

        return keys;
    }

    /**
     * Дебаг-метод
     */
    public static void debugComponents(@NonNull final ItemStack itemstack, @NonNull Player player) {
        if (itemstack == null || itemstack.getType().isAir()) {
            player.sendMessage("§cПредмет пустой");
            return;
        }

        Set<NamespacedKey> keys = getComponents(itemstack);

        player.sendMessage("§6=== Компоненты §e" + itemstack.getType() + " §6===");
        if (keys.isEmpty()) {
            player.sendMessage("§7Нет компонентов");
            player.sendMessage("§7Строка: §f" + itemstack.getItemMeta().getAsComponentString());
            return;
        }

        player.sendMessage("§7Найдено: §f" + keys.size());
        for (NamespacedKey key : keys) {
            player.sendMessage(" §8• §b" + key.getNamespace() + "§7:§f" + key.getKey());
        }
    }

    // ==================== getArmorComponents ====================

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