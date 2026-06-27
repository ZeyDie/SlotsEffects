package com.zeydie.slotseffect.bukkit.loaders;

import com.zeydie.slotseffect.bukkit.data.items.ItemEffectData;
import lombok.NonNull;
import org.bukkit.NamespacedKey;
import org.yaml.snakeyaml.Yaml;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public final class ItemEffectLoader {
    public static ItemEffectData load(@NonNull final Path path) {
        Yaml yaml = new Yaml();

        try (@NonNull InputStream inputStream = Files.newInputStream(path)) {
            Map<String, Object> root = yaml.load(inputStream);
            Map<String, Object> section = (Map<String, Object>) root.get("item-effect");

            ItemEffectData data = new ItemEffectData();

            data.setUuid(UUID.fromString((String) section.get("uuid")));

            Map<String, Object> component = (Map<String, Object>) section.get("component");
            data.setComponent(new NamespacedKey(
                    (String) component.get("namespace"),
                    (String) component.get("key")
            ));

            data.setSlots((List<String>) section.getOrDefault("slots", List.of()));

            data.setStaticEffects(EffectLoader.readEffects(
                    (List<Map<String, Object>>) section.getOrDefault("staticEffects", List.of())
            ));

            data.setAttackerEffects(EffectLoader.readEffects(
                    (List<Map<String, Object>>) section.getOrDefault("attackerEffects", List.of())
            ));

            data.setVictimEffects(EffectLoader.readEffects(
                    (List<Map<String, Object>>) section.getOrDefault("victimEffects", List.of())
            ));

            return data;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}
