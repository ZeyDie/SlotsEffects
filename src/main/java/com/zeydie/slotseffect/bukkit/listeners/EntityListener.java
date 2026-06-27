package com.zeydie.slotseffect.bukkit.listeners;

import com.zeydie.slotseffect.api.ArmorEffects;
import com.zeydie.slotseffect.api.ItemEffects;
import lombok.NonNull;
import lombok.val;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.jetbrains.annotations.Nullable;

public class EntityListener implements Listener {
    @EventHandler(priority = EventPriority.MONITOR)
    private void onAttackOfEntity(@NonNull final EntityDamageByEntityEvent event) {
        @NonNull val attacker = event.getDamager();
        @NonNull val victim = event.getEntity();

        if (!event.isCancelled() && attacker instanceof final Player attackerPlayer && victim instanceof final LivingEntity victimLivingEntity) {
            @NonNull val itemInHand = attackerPlayer.getInventory().getItemInMainHand();

            ItemEffects.applyAttackerEffects(attackerPlayer, itemInHand, EquipmentSlot.HAND);
            ItemEffects.applyVictimEffects(victimLivingEntity, itemInHand);

            if (victimLivingEntity instanceof final Player victimPlayer) {
                @NonNull val inventory = victimPlayer.getInventory();
                @NonNull val armorContents = inventory.getArmorContents();

                for (int i = 0; i < armorContents.length; i++) {
                    @Nullable val armorItem = armorContents[i];

                    if (armorItem != null)
                        ArmorEffects.applyHitEffects(victimPlayer, armorItem, i);
                }
            }
        }
    }
}