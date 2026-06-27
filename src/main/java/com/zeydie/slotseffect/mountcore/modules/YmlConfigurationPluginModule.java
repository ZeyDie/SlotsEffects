package com.zeydie.slotseffect.mountcore.modules;

import com.google.common.collect.Maps;
import com.zeydie.slotseffect.bukkit.data.armors.ArmorEffectData;
import com.zeydie.slotseffect.bukkit.data.armors.ArmorSetEffectData;
import com.zeydie.slotseffect.bukkit.data.items.ItemEffectData;
import com.zeydie.slotseffect.bukkit.loaders.ArmorEffectLoader;
import com.zeydie.slotseffect.bukkit.loaders.ArmorSetEffectLoader;
import com.zeydie.slotseffect.bukkit.loaders.ItemEffectLoader;
import com.zeydie.slotseffect.mountcore.utils.MountUtil;
import lombok.Getter;
import lombok.NonNull;
import lombok.val;
import org.bukkit.NamespacedKey;
import org.jetbrains.annotations.NotNull;
import ru.mountcode.plugins.mountcore.api.bootstrap.v1.IMountPlugin;
import ru.mountcode.plugins.mountcore.api.bootstrap.v1.module.IReloadableModule;
import ru.mountcode.plugins.mountcore.api.bootstrap.v1.module.PluginModule;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class YmlConfigurationPluginModule extends PluginModule implements IReloadableModule {
    @Getter
    private final @NotNull String name = "ymlconfig";

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

    public YmlConfigurationPluginModule(@NonNull final IMountPlugin plugin) {
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
        MountUtil.getLogger().info("YmlConfigurationPluginModule reload");

        this.initialize();

        return true;
    }

    @Override
    public void enable() {
        MountUtil.getLogger().info("YmlConfigurationPluginModule enabled");

        this.initialize();
    }

    private void initialize() {
        try {
            Files.list(this.itemsPath)
                    .filter(path -> path.toString().endsWith(".yml") || path.toString().endsWith(".yaml"))
                    .forEach(
                            path ->
                            {
                                MountUtil.getLogger().info("Loading " + path);

                                try {
                                    @NonNull val itemEffectData = ItemEffectLoader.load(path);
                                    @NonNull val component = itemEffectData.getComponent();
                                    @NonNull val list = this.itemsEffects.computeIfAbsent(component, k -> new ArrayList<>());

                                    if (list.isEmpty() || list.stream().noneMatch(existing -> existing.getUuid().equals(itemEffectData.getUuid())))
                                        list.add(itemEffectData);

                                    this.itemsEffects.put(component, list);
                                } catch (final Exception exception) {
                                    MountUtil.getLogger().error("Failed to load " + path, exception);
                                    exception.printStackTrace();
                                }
                            }
                    );

            Files.list(this.armorsPath)
                    .filter(path -> path.toString().endsWith(".yml") || path.toString().endsWith(".yaml"))
                    .forEach(
                            path ->
                            {
                                MountUtil.getLogger().info("Loading " + path);

                                try {
                                    @NonNull val armorEffectData = ArmorEffectLoader.load(path);
                                    @NonNull val component = armorEffectData.getComponent();
                                    @NonNull val list = this.armorEffects.computeIfAbsent(component, k -> new ArrayList<>());

                                    if (list.isEmpty() || list.stream().noneMatch(existing -> existing.getUuid().equals(armorEffectData.getUuid())))
                                        list.add(armorEffectData);

                                    this.armorEffects.put(component, list);
                                } catch (final Exception exception) {
                                    MountUtil.getLogger().error("Failed to load " + path, exception);
                                }
                            }
                    );

            Files.list(this.armorsetsPath)
                    .filter(path -> path.toString().endsWith(".yml") || path.toString().endsWith(".yaml"))
                    .forEach(
                            path ->
                            {
                                MountUtil.getLogger().info("Loading " + path);

                                try {
                                    @NonNull val armorSetEffectData = ArmorSetEffectLoader.load(path);
                                    @NonNull val component = armorSetEffectData.getComponent();
                                    @NonNull val list = this.armorSetsEffects.computeIfAbsent(component, k -> new ArrayList<>());

                                    if (list.isEmpty() || list.stream().noneMatch(existing -> existing.getUuid().equals(armorSetEffectData.getUuid())))
                                        list.add(armorSetEffectData);

                                    this.armorSetsEffects.put(component, list);
                                } catch (final Exception exception) {
                                    MountUtil.getLogger().error("Failed to load " + path, exception);
                                }
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