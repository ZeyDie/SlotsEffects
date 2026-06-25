package com.zeydie.slotseffect.bukkit.data;

import lombok.Getter;
import lombok.NonNull;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.EquipmentSlot;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Getter
public class ArmorEffectData extends EffectData {
    private @NotNull NamespacedKey component;
    private @Nullable EquipmentSlot equipmentSlot;
}