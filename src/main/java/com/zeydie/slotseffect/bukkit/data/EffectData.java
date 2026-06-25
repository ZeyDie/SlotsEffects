package com.zeydie.slotseffect.bukkit.data;

import lombok.Getter;
import lombok.NonNull;
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

    private @NotNull List<String> slots = List.of("ALL");

    private @Nullable List<PotionEffectData> staticEffects;
    private @Nullable List<PotionEffectData> attackerEffects;
    private @Nullable List<PotionEffectData> victimEffects;
}