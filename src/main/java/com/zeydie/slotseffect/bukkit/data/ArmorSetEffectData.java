package com.zeydie.slotseffect.bukkit.data;

import lombok.Getter;
import lombok.NonNull;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.EquipmentSlot;

import java.util.Map;

@Getter
public class ArmorSetEffectData extends EffectData {
    private @NonNull Map<EquipmentSlot, NamespacedKey> equipmentSlotsWithComponents;
}