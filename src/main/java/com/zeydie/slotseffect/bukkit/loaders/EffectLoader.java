package com.zeydie.slotseffect.bukkit.loaders;

import com.zeydie.slotseffect.bukkit.data.objects.PotionEffectData;
import lombok.val;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public final class EffectLoader {
    public static List<PotionEffectData> readEffects(List<Map<String, Object>> list) {
        return list.stream()
                .map(EffectLoader::readEffect)
                .collect(Collectors.toList());
    }

    public static PotionEffectData readEffect(Map<String, Object> map) {
        val chance = map.containsKey("chance") ? (int) map.get("chance") : 0;

        return new PotionEffectData(
                chance,
                (String) map.get("type"),
                ((Number) map.get("amplifier")).intValue(),
                ((Number) map.get("duration")).intValue()
        );
    }
}
