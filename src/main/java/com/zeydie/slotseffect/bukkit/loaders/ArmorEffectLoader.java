package com.zeydie.slotseffect.bukkit.loaders;

import com.zeydie.slotseffect.bukkit.data.armors.ArmorEffectData;
import com.zeydie.slotseffect.bukkit.data.items.ItemEffectData;
import lombok.NonNull;
import lombok.val;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.EquipmentSlot;
import org.jetbrains.annotations.Nullable;
import org.yaml.snakeyaml.Yaml;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public final class ArmorEffectLoader {
    public static List<ArmorEffectData> load(@NonNull final Path path) {
        Yaml yaml = new Yaml();

        try (@NonNull InputStream inputStream = Files.newInputStream(path)) {
            Map<String, Object> root = yaml.load(inputStream);

            List<Map<String, Object>> sections =
                    (List<Map<String, Object>>) root.getOrDefault("armor-effect", List.of());

            List<ArmorEffectData> result = new ArrayList<>();

            for (Map<String, Object> section : sections) {
                ArmorEffectData data = new ArmorEffectData();

                data.setUuid(UUID.fromString((String) section.get("uuid")));

                Map<String, Object> component = (Map<String, Object>) section.get("component");
                data.setComponent(new NamespacedKey(
                        (String) component.get("namespace"),
                        (String) component.get("key")
                ));

                @Nullable val stringEqSlot = section.getOrDefault("equipmentSlot", null);

                if (stringEqSlot != null)
                    data.setEquipmentSlot(EquipmentSlot.valueOf((String) stringEqSlot));

                data.setStaticEffects(EffectLoader.readEffects(
                        (List<Map<String, Object>>) section.getOrDefault("staticEffects", List.of())
                ));
                data.setHitEffects(EffectLoader.readEffects(
                        (List<Map<String, Object>>) section.getOrDefault("hitEffects", List.of())
                ));
            }

            return result;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}
