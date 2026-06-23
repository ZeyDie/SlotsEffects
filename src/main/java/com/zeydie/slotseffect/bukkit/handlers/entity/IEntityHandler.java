package com.zeydie.slotseffect.bukkit.handlers.entity;

import lombok.NonNull;
import org.bukkit.entity.Entity;

public interface IEntityHandler {
    void handle(@NonNull final Entity entity);
}