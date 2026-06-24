package com.zeydie.slotseffect.bukkit.data;

import com.zeydie.slotseffect.mountcore.SlotsEffect;
import lombok.NonNull;
import lombok.val;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.Nullable;

public record PotionEffectData
        (
                @NonNull String type,
                int amplifier,
                int duration
        ) {
    public @Nullable PotionEffect createPotionEffect() {
        @Nullable val effect = PotionEffectType.getByName(this.type);

        if (effect == null) {
            SlotsEffect.getInstance().logger().warn("PotionEffectType " + this.type + " not found!");
            return null;
        }

        return effect.createEffect(this.duration, this.amplifier);
    }
}