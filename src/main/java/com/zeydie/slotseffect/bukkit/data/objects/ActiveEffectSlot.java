package com.zeydie.slotseffect.bukkit.data.objects;

import lombok.NonNull;
import org.bukkit.potion.PotionEffect;

public record ActiveEffectSlot
        (
                @NonNull String slot,
                @NonNull PotionEffect potionEffect
        ) {
}