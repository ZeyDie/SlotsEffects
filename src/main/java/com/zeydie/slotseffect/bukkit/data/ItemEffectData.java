package com.zeydie.slotseffect.bukkit.data;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.EquipmentSlot;
import org.jetbrains.annotations.Nullable;

@Setter
@Getter
public class ItemEffectData extends EffectData {
    private @NonNull NamespacedKey component;
    private @Nullable EquipmentSlot equipmentSlot;
}