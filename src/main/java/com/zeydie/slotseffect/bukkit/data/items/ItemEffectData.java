package com.zeydie.slotseffect.bukkit.data.items;

import com.zeydie.slotseffect.bukkit.data.EffectData;
import com.zeydie.slotseffect.bukkit.data.objects.PotionEffectData;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.NamespacedKey;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

@Setter
@Getter
public class ItemEffectData extends EffectData {
    private @NotNull NamespacedKey component;
    private @NotNull List<String> slots = List.of("ALL");

    private @Nullable List<PotionEffectData> attackerEffects = List.of();
    private @Nullable List<PotionEffectData> victimEffects = List.of();
}