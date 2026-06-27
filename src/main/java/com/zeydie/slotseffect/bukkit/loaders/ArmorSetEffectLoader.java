package com.zeydie.slotseffect.bukkit.loaders;

import com.zeydie.slotseffect.bukkit.data.armors.ArmorSetEffectData;
import lombok.NonNull;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.EquipmentSlot;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.yaml.snakeyaml.Yaml;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public final class ArmorSetEffectLoader {
    public static List<ArmorSetEffectData> load(@NonNull final Path path) {
        Yaml yaml = new Yaml();

        try (@NonNull InputStream inputStream = Files.newInputStream(path)) {
            Map<String, Object> root = yaml.load(inputStream);
            List<Map<String, Object>> sections =
                    (List<Map<String, Object>>) root.getOrDefault("armor-set-effect", List.of());

            List<ArmorSetEffectData> result = new ArrayList<>();

            for (Map<String, Object> section : sections) {
                ArmorSetEffectData data = new ArmorSetEffectData();

                data.setUuid(UUID.fromString((String) section.get("uuid")));

                Map<String, Object> component = (Map<String, Object>) section.get("component");
                data.setComponent(new NamespacedKey(
                        (String) component.get("namespace"),
                        (String) component.get("key")
                ));

                data.setEquipmentSlotsWithComponents(
                        readEquipmentSlotsWithComponents(
                                (Map<String, Map<String, String>>) section.get("equipmentSlotsWithComponents")
                        )
                );
                data.setStaticEffects(EffectLoader.readEffects(
                        (List<Map<String, Object>>) section.getOrDefault("staticEffects", List.of())
                ));
                data.setHitEffects(EffectLoader.readEffects(
                        (List<Map<String, Object>>) section.getOrDefault("hitEffects", List.of())
                ));

                result.add(data);
            }

            return result;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static @NotNull Map<EquipmentSlot, NamespacedKey> readEquipmentSlotsWithComponents(
            @Nullable Map<String, Map<String, String>> sections
    ) {
        if (sections == null || sections.isEmpty()) {
            return Map.of();
        }

        Map<EquipmentSlot, NamespacedKey> result = new EnumMap<>(EquipmentSlot.class);

        for (Map.Entry<String, Map<String, String>> entry : sections.entrySet()) {
            Map<String, String> component = entry.getValue();

            result.put(
                    EquipmentSlot.valueOf(entry.getKey()),
                    new NamespacedKey(
                            component.get("namespace"),
                            component.get("key")
                    )
            );
        }

        return result;
    }
}