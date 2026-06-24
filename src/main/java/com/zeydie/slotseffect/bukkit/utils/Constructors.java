package com.zeydie.slotseffect.bukkit.utils;

import lombok.NonNull;
import lombok.val;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class Constructors {
    public static final @NotNull String getPathOfEffect(@NonNull final PotionEffectType potionEffectType) {
        @NonNull val category = potionEffectType.getCategory().name();
        @NonNull val id = potionEffectType.getName();

        return category + "." + id;
    }

    public static final @Nullable PotionEffectType getEffectOfPath(@NonNull final String path) {
        @NonNull val name = path.contains(".") ? path.split("\\.")[1] : path;

        return PotionEffectType.getByName(name);
    }
}