package com.zeydie.slotseffect.bukkit.handlers.entity;

import lombok.Getter;
import lombok.NonNull;
import org.bukkit.entity.Entity;

public final class AttackerHandler implements IEntityHandler {
    @Getter
    private static final AttackerHandler instance = new AttackerHandler();

    @Override
    public void handle(@NonNull final Entity entity) {

    }
}