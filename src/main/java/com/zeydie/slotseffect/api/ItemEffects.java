package com.zeydie.slotseffect.api;

import com.zeydie.slotseffect.bukkit.utils.ItemUtil;
import com.zeydie.slotseffect.mountcore.SlotsEffect;
import lombok.NonNull;
import lombok.val;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public final class ItemEffects {
    public static @NotNull List<PotionEffect> getStaticEffects(@NonNull final ItemStack itemstack) {
        @NonNull val components = ItemUtil.getComponents(itemstack);

        if (components.isEmpty()) return List.of();

        @NonNull val potionEffects = new ArrayList<PotionEffect>();

        @NonNull val itemEffects = SlotsEffect.getInstance()
                .getConfigurationModule()
                .getItemsEffects();

        components.forEach(
                component -> {
                    itemEffects.get(component)
                            .forEach(
                                    effectData -> effectData.getStaticEffects()
                                            .forEach(
                                                    potionEffectData ->
                                                    {
                                                        @Nullable val potionEffect = potionEffectData.createPotionEffect();

                                                        if (potionEffect != null)
                                                            potionEffects.add(potionEffect);
                                                    }
                                            )
                            );

                }
        );

        return potionEffects;
    }

    public static @NotNull List<PotionEffect> getAttackerEffects(@NonNull final ItemStack itemstack) {
        @NonNull val components = ItemUtil.getComponents(itemstack);

        if (components.isEmpty()) return List.of();

        @NonNull val potionEffects = new ArrayList<PotionEffect>();

        @NonNull val itemEffects = SlotsEffect.getInstance()
                .getConfigurationModule()
                .getItemsEffects();

        components.forEach(
                component -> {
                    itemEffects.get(component)
                            .forEach(
                                    effectData -> effectData.getAttackerEffects()
                                            .forEach(
                                                    potionEffectData ->
                                                    {
                                                        @Nullable val potionEffect = potionEffectData.createPotionEffect();

                                                        if (potionEffect != null)
                                                            potionEffects.add(potionEffect);
                                                    }
                                            )
                            );

                }
        );

        return potionEffects;
    }

    public static @NotNull List<PotionEffect> getVictimEffects(@NonNull final ItemStack itemstack) {
        @NonNull val components = ItemUtil.getComponents(itemstack);

        if (components.isEmpty()) return List.of();

        @NonNull val potionEffects = new ArrayList<PotionEffect>();

        @NonNull val itemEffects = SlotsEffect.getInstance()
                .getConfigurationModule()
                .getItemsEffects();

        components.forEach(
                component -> {
                    itemEffects.get(component)
                            .forEach(
                                    effectData -> effectData.getVictimEffects()
                                            .forEach(
                                                    potionEffectData ->
                                                    {
                                                        @Nullable val potionEffect = potionEffectData.createPotionEffect();

                                                        if (potionEffect != null)
                                                            potionEffects.add(potionEffect);
                                                    }
                                            )
                            );

                }
        );

        return potionEffects;
    }
}