package com.zeydie.slotseffect.bukkit.data;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.NamespacedKey;
import org.jetbrains.annotations.NotNull;

import java.util.List;

@Setter
@Getter
public class ItemEffectData extends EffectData {
    private @NotNull NamespacedKey component;
    private @NotNull List<String> slots = List.of("ALL");
}