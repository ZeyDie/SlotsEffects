package com.zeydie.slotseffect.test;

import lombok.NonNull;
import org.bukkit.NamespacedKey;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

import static com.zeydie.slotseffect.bukkit.utils.ItemUtil.getComponents;

public class SlotsCommand extends Command implements CommandExecutor {

    public SlotsCommand(@NotNull String name) {
        super(name);
    }

    @Override
    public boolean onCommand(@NonNull CommandSender sender, @NonNull Command command,
                             @NonNull String label, @NotNull String[] args) {

        if (!(sender instanceof Player player)) {
            sender.sendMessage("§cКоманда только для игроков!");
            return true;
        }

        if (args.length == 0 || args[0].equalsIgnoreCase("debug")) {
            ItemStack item = player.getInventory().getItemInMainHand();

            if (item == null || item.getType().isAir()) {
                player.sendMessage("§cВозьми предмет в руку!");
                return true;
            }

            player.sendMessage("§6=== Компоненты предмета §e" + item.getType() + " §6===");
            debugComponents(item, player);
            return true;
        }

        player.sendMessage("§cИспользование: /slots debug");
        return true;
    }

    public static void debugComponents(@NonNull final ItemStack itemstack, @NonNull Player player) {
        if (itemstack == null || itemstack.getType().isAir()) {
            player.sendMessage("§cПредмет пустой");
            return;
        }

        Set<NamespacedKey> keys = getComponents(itemstack);

        player.sendMessage("§6=== Компоненты §e" + itemstack.getType() + " §6===");
        if (keys.isEmpty()) {
            player.sendMessage("§7Нет компонентов в minecraft:custom_data");
            return;
        }

        player.sendMessage("§7Найдено: §f" + keys.size());
        for (NamespacedKey key : keys) {
            player.sendMessage(" §8• §b" + key.getNamespace() + "§7:§f" + key.getKey());
        }
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String commandLabel, @NotNull String @NotNull [] args) {
        return false;
    }
}