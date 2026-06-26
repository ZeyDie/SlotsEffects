package com.zeydie.slotseffect.mountcore.modules;

import com.google.common.collect.Maps;
import com.zeydie.sgson.SGsonFile;
import com.zeydie.slotseffect.bukkit.data.armors.ArmorEffectData;
import com.zeydie.slotseffect.bukkit.data.armors.ArmorSetEffectData;
import com.zeydie.slotseffect.bukkit.data.items.ItemEffectData;
import com.zeydie.slotseffect.bukkit.data.objects.PotionEffectData;
import com.zeydie.slotseffect.mountcore.SlotsEffect;
import com.zeydie.slotseffect.mountcore.utils.MountUtil;
import lombok.Getter;
import lombok.NonNull;
import lombok.val;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;
import ru.mountcode.plugins.mountcore.api.bootstrap.v1.IMountPlugin;
import ru.mountcode.plugins.mountcore.api.bootstrap.v1.module.IReloadableModule;
import ru.mountcode.plugins.mountcore.api.bootstrap.v1.module.PluginModule;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class GsonConfigurationPluginModule extends PluginModule implements IReloadableModule {
    @Getter
    private final @NotNull String name = "gsonconfig";

    @Getter
    private final @NotNull Path directory;
    @Getter
    private final @NotNull Path itemsPath;
    @Getter
    private final @NotNull Path armorsPath;
    @Getter
    private final @NotNull Path armorsetsPath;

    @Getter
    private final @NotNull Map<NamespacedKey, List<ItemEffectData>> itemsEffects = Maps.newHashMap();
    @Getter
    private final @NotNull Map<NamespacedKey, List<ArmorEffectData>> armorEffects = Maps.newHashMap();
    @Getter
    private final @NotNull Map<NamespacedKey, List<ArmorSetEffectData>> armorSetsEffects = Maps.newHashMap();

    public GsonConfigurationPluginModule(@NonNull final IMountPlugin plugin) {
        super(plugin);

        this.directory = MountUtil.getPluginDirectory(this);
        this.itemsPath = this.directory.resolve("items");
        this.armorsPath = this.directory.resolve("armors");
        this.armorsetsPath = this.directory.resolve("armorsets");

        try {
            if (!Files.exists(this.itemsPath)) Files.createDirectories(this.itemsPath);
            if (!Files.exists(this.armorsPath)) Files.createDirectories(this.armorsPath);
            if (!Files.exists(this.armorsetsPath)) Files.createDirectories(this.armorsetsPath);
        } catch (final Exception exception) {
            exception.printStackTrace();
        }
    }

    @Override
    public boolean reload() {
        return true;
    }

    @Override
    public void enable() {
        try {
            SlotsEffect.getInstance().logger().info("GsonConfigurationPluginModule enabled");

            if (Files.list(this.itemsPath).count() == 0) {
                @NonNull val example = new ItemEffectData();

                example.setComponent(new NamespacedKey("namespace", "diamond"));
                example.setSlots(List.of(EquipmentSlot.HAND.name()));
                example.setStaticEffects(List.of(new PotionEffectData(1, PotionEffectType.SPEED.getName(), 3, PotionEffect.INFINITE_DURATION)));
                example.setVictimEffects(List.of(new PotionEffectData(0.5, PotionEffectType.BLINDNESS.getName(), 3, 60)));
                example.setAttackerEffects(List.of(new PotionEffectData(0.7, PotionEffectType.STRENGTH.getName(), 3, 60)));

                SGsonFile.createPretty(this.itemsPath.resolve("example.json")).writeJsonFile(example);
            }
            if (Files.list(this.armorsPath).count() == 0) {
                @NonNull val example = new ArmorEffectData();

                @NonNull val equipmentSlotsWithComponents = Maps.<EquipmentSlot, NamespacedKey>newHashMap();

                example.setEquipmentSlot(EquipmentSlot.HEAD);
                example.setComponent(new NamespacedKey("namespace", "helmet"));
                example.setStaticEffects(List.of(new PotionEffectData(1, PotionEffectType.HEALTH_BOOST.getName(), 3, 100)));
                example.setHitEffects(List.of(new PotionEffectData(0.2, PotionEffectType.REGENERATION.getName(), 3, 100)));

                SGsonFile.createPretty(this.armorsPath.resolve("example.json")).writeJsonFile(example);
            }
            if (Files.list(this.armorsetsPath).count() == 0) {
                @NonNull val example = new ArmorSetEffectData();

                @NonNull val equipmentSlotsWithComponents = Maps.<EquipmentSlot, NamespacedKey>newHashMap();

                equipmentSlotsWithComponents.put(EquipmentSlot.HEAD, new NamespacedKey("namespace", "helmet"));

                example.setEquipmentSlotsWithComponents(equipmentSlotsWithComponents);
                example.setStaticEffects(List.of(new PotionEffectData(1, PotionEffectType.HEALTH_BOOST.getName(), 3, 3)));

                SGsonFile.createPretty(this.armorsetsPath.resolve("example.json")).writeJsonFile(example);
            }

            Files.list(this.itemsPath)
                    .filter(path -> path.toString().endsWith(".json"))
                    .forEach(
                            path ->
                            {
                                SlotsEffect.getInstance().logger().info("Loading " + path);

                                @NonNull val itemEffectData = SGsonFile.createPretty(path).fromJsonToObject(new ItemEffectData());
                                @NonNull val list = this.itemsEffects.getOrDefault(itemEffectData, new ArrayList<>());

                                if (list.isEmpty() || list.stream().noneMatch(effectData -> effectData.getUuid().equals(itemEffectData.getUuid())))
                                    list.add(itemEffectData);

                                this.itemsEffects.put(itemEffectData.getComponent(), list);
                            }
                    );

            Files.list(this.armorsPath)
                    .filter(path -> path.toString().endsWith(".json"))
                    .forEach(
                            path ->
                            {
                                SlotsEffect.getInstance().logger().info("Loading " + path);

                                @NonNull val armorEffectData = SGsonFile.createPretty(path).fromJsonToObject(new ArmorEffectData());
                                @NonNull val list = this.armorEffects.getOrDefault(armorEffectData, new ArrayList<>());

                                if (list.isEmpty() || list.stream().noneMatch(effectData -> effectData.getUuid().equals(armorEffectData.getUuid())))
                                    list.add(armorEffectData);

                                this.armorEffects.put(armorEffectData.getComponent(), list);
                            }
                    );

            Files.list(this.armorsetsPath)
                    .filter(path -> path.toString().endsWith(".json"))
                    .forEach(
                            path ->
                            {
                                SlotsEffect.getInstance().logger().info("Loading " + path);

                                @NonNull val armorSetEffectData = SGsonFile.createPretty(path).fromJsonToObject(new ArmorSetEffectData());
                                @NonNull val list = this.armorSetsEffects.getOrDefault(armorSetEffectData, new ArrayList<>());

                                if (list.isEmpty() || list.stream().noneMatch(effectData -> effectData.getUuid().equals(armorSetEffectData.getUuid())))
                                    list.add(armorSetEffectData);

                                //this.armorSetsEffects.put(armorSetEffectData., list);
                            }
                    );
        } catch (final Exception exception) {
            exception.printStackTrace();
        }
    }

    @Override
    public void shutdown() {
        this.itemsEffects.clear();
        this.armorEffects.clear();
        this.armorSetsEffects.clear();
    }
}
