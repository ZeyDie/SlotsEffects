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

@Setter
@Getter
public class ArmorEffectData extends EffectData {
    private @NotNull NamespacedKey component;
    private @Nullable EquipmentSlot equipmentSlot;

    private @Nullable List<PotionEffectData> hitEffects;
}