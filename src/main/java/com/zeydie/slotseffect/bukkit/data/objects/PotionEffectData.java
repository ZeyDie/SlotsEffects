package com.zeydie.slotseffect.bukkit.data.objects;

import com.zeydie.slotseffect.mountcore.SlotsEffect;
import com.zeydie.slotseffect.mountcore.utils.MountUtil;
import lombok.val;
import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.bukkit.potion.PotionEffect;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public record PotionEffectData
        (
                double chance,
                @NotNull String type,
                int amplifier,
                int duration
        ) {
    public @Nullable PotionEffect createPotionEffect() {
       @Nullable val effect = Registry.EFFECT.get(NamespacedKey.minecraft(this.type.toLowerCase()));

        if (effect == null) {
            MountUtil.getLogger().warn("PotionEffectType " + this.type + " not found!");
            return null;
        }

        return effect.createEffect(this.duration, this.amplifier);
    }
}