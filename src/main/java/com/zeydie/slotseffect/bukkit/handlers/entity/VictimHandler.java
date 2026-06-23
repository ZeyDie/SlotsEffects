package com.zeydie.slotseffect.bukkit.handlers.entity;

import lombok.Getter;
import lombok.NonNull;
import org.bukkit.entity.Entity;

public final class VictimHandler implements IEntityHandler {
    @Getter
    private static final VictimHandler instance = new VictimHandler();

    @Override
    public void handle(@NonNull final Entity entity) {

    }
}