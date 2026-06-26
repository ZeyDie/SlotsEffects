package com.zeydie.slotseffect.bukkit.data;

import com.zeydie.slotseffect.mountcore.SlotsEffect;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public record PotionEffectData
        (
                @NotNull PotionEffectType type,
                int amplifier,
                int duration
        ) {
    public @Nullable PotionEffect createPotionEffect() {
        //@Nullable val effect = PotionEffectType.getByName(this.type);

        if (type == null) {
            SlotsEffect.getInstance().logger().warn("PotionEffectType " + this.type + " not found!");
            return null;
        }

        return this.type.createEffect(this.duration, this.amplifier);
    }
}