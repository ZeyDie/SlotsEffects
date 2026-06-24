package com.zeydie.slotseffect.bukkit.listeners;

import com.zeydie.slotseffect.api.ItemEffects;
import com.zeydie.slotseffect.mountcore.SlotsEffect;
import lombok.NonNull;
import lombok.val;
import org.bukkit.Bukkit;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class EntityListener implements Listener {
    @EventHandler(priority = EventPriority.MONITOR)
    private void onAttackOfEntity(@NonNull final EntityDamageByEntityEvent event) {
        @NonNull val attacker = event.getDamager();
        @NonNull val victim = event.getEntity();

        if (!event.isCancelled() && attacker instanceof final LivingEntity attackerLivingEntity && victim instanceof final LivingEntity victimLivingEntity) {
            @NonNull val activeItem = attackerLivingEntity.getActiveItem();

            Bukkit.getScheduler().runTaskLater(
                    SlotsEffect.getInstance(),
                    () -> {
                        ItemEffects.getAttackerEffects(activeItem).forEach(effect -> attackerLivingEntity.addPotionEffect(effect));
                        ItemEffects.getVictimEffects(activeItem).forEach(effect -> victimLivingEntity.addPotionEffect(effect));
                    },
                    1
            );
        }
    }
}