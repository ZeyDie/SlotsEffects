package com.zeydie.slotseffect.bukkit.data;

import com.zeydie.slotseffect.bukkit.data.objects.PotionEffectData;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.UUID;

@Setter
@Getter
public class EffectData {
    private @NotNull UUID uuid = UUID.randomUUID();
    private @Nullable List<String> comments;

    private @Nullable List<PotionEffectData> staticEffects;
}