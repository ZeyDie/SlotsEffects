package com.zeydie.slotseffect.mountcore.modules;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.zeydie.sgson.SGsonFile;
import com.zeydie.slotseffect.bukkit.data.*;
import com.zeydie.slotseffect.mountcore.utils.MountUtil;
import lombok.Getter;
import lombok.NonNull;
import lombok.val;
import org.bukkit.NamespacedKey;
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
    private final @NotNull Map<NamespacedKey, List<EffectData>> itemsEffects = Maps.newHashMap();
    @Getter
    private final @NotNull Map<NamespacedKey, List<EffectData>> armorEffects = Maps.newHashMap();
    @Getter
    private final @NotNull Map<NamespacedKey, List<EffectData>> armorSetsEffects = Maps.newHashMap();

    public GsonConfigurationPluginModule(@NonNull final IMountPlugin plugin) {
        super(plugin);

        this.directory = MountUtil.getPluginDirectory(this);
        this.itemsPath = this.directory.resolve("items");
        this.armorsPath = this.directory.resolve("armors");
        this.armorsetsPath = this.directory.resolve("armorsets");

    }

    @Override
    public boolean reload() {
        return true;
    }

    @Override
    public void enable() {
        try {
            if (Files.list(this.itemsPath).count() == 0) {
                @NonNull val example = new ItemEffectData();

                example.setComponent(new NamespacedKey("namespace", "key"));
                example.setStaticEffects(List.of(new PotionEffectData(PotionEffectType.SPEED.getName(), 3, PotionEffect.INFINITE_DURATION)));

                SGsonFile.createPretty(this.itemsPath.resolve("example.json")).writeJsonFile(example);
            }

            Files.walk(this.itemsPath)
                    .forEach(
                            path ->
                            {
                                @NonNull val itemEffectData = SGsonFile.createPretty(path).fromJsonToObject(new ItemEffectData());
                                @NonNull val list = this.itemsEffects.getOrDefault(itemEffectData, new ArrayList<>());

                                if (list.isEmpty() || list.stream().noneMatch(effectData -> effectData.getUuid().equals(itemEffectData.getUuid())))
                                    list.add(itemEffectData);

                                this.itemsEffects.put(itemEffectData.getComponent(), list);
                            }
                    );

            Files.walk(this.armorsPath)
                    .forEach(
                            path ->
                            {
                                @NonNull val armorEffectData = SGsonFile.createPretty(path).fromJsonToObject(new ArmorEffectData());
                                @NonNull val list = this.armorEffects.getOrDefault(armorEffectData, new ArrayList<>());

                                if (list.isEmpty() || list.stream().noneMatch(effectData -> effectData.getUuid().equals(armorEffectData.getUuid())))
                                    list.add(armorEffectData);

                                this.armorEffects.put(armorEffectData.getComponent(), list);
                            }
                    );

            Files.walk(this.armorsetsPath)
                    .forEach(
                            path ->
                            {
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

    }
}
