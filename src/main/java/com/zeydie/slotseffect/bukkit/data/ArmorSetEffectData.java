package com.zeydie.slotseffect.bukkit.data;

import lombok.Getter;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.EquipmentSlot;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

@Getter
public class ArmorSetEffectData extends EffectData {
    private @NotNull Map<EquipmentSlot, NamespacedKey> equipmentSlotsWithComponents;
}