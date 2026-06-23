package com.zeydie.slotseffect.bukkit.listeners;

import com.zeydie.slotseffect.bukkit.handlers.entity.AttackerHandler;
import com.zeydie.slotseffect.bukkit.handlers.entity.VictimHandler;
import lombok.NonNull;
import lombok.val;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.jetbrains.annotations.NotNull;

public class EntityListener implements Listener {
    private final @NotNull AttackerHandler attackerHandler = AttackerHandler.getInstance();
    private final @NotNull VictimHandler victimHandler = VictimHandler.getInstance();

    @EventHandler(priority = EventPriority.MONITOR)
    private void onAttackOfEntity(@NonNull final EntityDamageByEntityEvent event) {
        @NonNull val attacker = event.getDamager();
        @NonNull val victim = event.getEntity();

        this.attackerHandler.handle(attacker);
        this.victimHandler.handle(victim);
    }
}