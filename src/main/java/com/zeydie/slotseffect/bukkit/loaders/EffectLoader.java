package com.zeydie.slotseffect.bukkit.loaders;

import com.zeydie.slotseffect.bukkit.data.objects.PotionEffectData;

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
        return new PotionEffectData(
                ((Number) map.get("chance")).doubleValue(),
                (String) map.get("type"),
                ((Number) map.get("amplifier")).intValue(),
                ((Number) map.get("duration")).intValue()
        );
    }
}
