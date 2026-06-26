package com.zeydie.slotseffect.bukkit.data.armors;

import com.zeydie.slotseffect.bukkit.data.EffectData;
import com.zeydie.slotseffect.bukkit.data.objects.PotionEffectData;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.EquipmentSlot;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;

@Setter
@Getter
public class ArmorSetEffectData extends EffectData {
    private @NotNull Map<EquipmentSlot, NamespacedKey> equipmentSlotsWithComponents;

    private @Nullable List<PotionEffectData> hitEffects;
}