package com.zeydie.slotseffect.mountcore.modules;

import com.zeydie.slotseffect.api.ArmorEffects;
import com.zeydie.slotseffect.api.ItemEffects;
import com.zeydie.slotseffect.bukkit.loaders.ArmorEffectLoader;
import com.zeydie.slotseffect.bukkit.loaders.ArmorSetEffectLoader;
import com.zeydie.slotseffect.bukkit.loaders.ItemEffectLoader;
import lombok.Getter;
import lombok.NonNull;
import lombok.val;
import org.jetbrains.annotations.NotNull;
import ru.mountcode.plugins.mountcore.api.bootstrap.v1.IMountPlugin;
import ru.mountcode.plugins.mountcore.api.bootstrap.v1.module.IReloadableModule;
import ru.mountcode.plugins.mountcore.api.bootstrap.v1.module.PluginModule;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;

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

    public YmlConfigurationPluginModule(@NonNull final IMountPlugin plugin) {
        super(plugin);

        this.directory = this.getPlugin().getPluginDirectory();
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
        this.getPlugin().logger().info("YmlConfigurationPluginModule reload");

        this.initialize();

        return true;
    }

    @Override
    public void enable() {
        this.getPlugin().logger().info("YmlConfigurationPluginModule enabled");

        this.initialize();
    }

    private void initialize() {
        ItemEffects.itemsEffects.clear();
        ArmorEffects.armorEffects.clear();
        ArmorEffects.armorSetsEffects.clear();

        try (@NonNull val stream = Files.list(this.itemsPath)) {
            stream
                    .filter(path -> path.toString().endsWith(".yml") || path.toString().endsWith(".yaml"))
                    .forEach(
                            path ->
                            {
                                this.getPlugin().logger().info("Loading " + path);

                                try {
                                    @NonNull val itemEffectDatas = ItemEffectLoader.load(path);

                                    for (@NonNull val itemEffectData : itemEffectDatas) {
                                        @NonNull val component = itemEffectData.getComponent();
                                        @NonNull val list = ItemEffects.itemsEffects.computeIfAbsent(component, k -> new ArrayList<>());

                                        if (list.isEmpty() || list.stream().noneMatch(existing -> existing.getUuid().equals(itemEffectData.getUuid())))
                                            list.add(itemEffectData);

                                        ItemEffects.itemsEffects.put(component, list);
                                    }
                                } catch (final Exception exception) {
                                    this.getPlugin().logger().error("Failed to load " + path, exception);
                                }
                            }
                    );
        } catch (IOException e) {
            e.printStackTrace();
        }

        try (@NonNull val stream = Files.list(this.armorsetsPath)) {
            stream
                    .filter(path -> path.toString().endsWith(".yml") || path.toString().endsWith(".yaml"))
                    .forEach(
                            path ->
                            {
                                this.getPlugin().logger().info("Loading " + path);

                                try {
                                    @NonNull val armorEffectDatas = ArmorEffectLoader.load(path);

                                    for (@NonNull val armorEffectData : armorEffectDatas) {
                                        @NonNull val component = armorEffectData.getComponent();
                                        @NonNull val list = ArmorEffects.armorEffects.computeIfAbsent(component, k -> new ArrayList<>());

                                        if (list.isEmpty() || list.stream().noneMatch(existing -> existing.getUuid().equals(armorEffectData.getUuid())))
                                            list.add(armorEffectData);

                                        ArmorEffects.armorEffects.put(component, list);
                                    }
                                } catch (final Exception exception) {
                                    this.getPlugin().logger().error("Failed to load " + path, exception);
                                }
                            }
                    );
        } catch (IOException e) {
            e.printStackTrace();
        }

        try (@NonNull val stream = Files.list(this.armorsPath)) {
            stream
                    .filter(path -> path.toString().endsWith(".yml") || path.toString().endsWith(".yaml"))
                    .forEach(
                            path ->
                            {
                                this.getPlugin().logger().info("Loading " + path);

                                try {
                                    @NonNull val armorSetEffectDatas = ArmorSetEffectLoader.load(path);

                                    for (@NonNull val armorSetEffectData : armorSetEffectDatas) {
                                        @NonNull val component = armorSetEffectData.getComponent();
                                        @NonNull val list = ArmorEffects.armorSetsEffects.computeIfAbsent(component, k -> new ArrayList<>());

                                        if (list.isEmpty() || list.stream().noneMatch(existing -> existing.getUuid().equals(armorSetEffectData.getUuid())))
                                            list.add(armorSetEffectData);

                                        ArmorEffects.armorSetsEffects.put(component, list);
                                    }
                                } catch (final Exception exception) {
                                    this.getPlugin().logger().error("Failed to load " + path, exception);
                                }
                            }
                    );
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void shutdown() {
        ItemEffects.itemsEffects.clear();
        ArmorEffects.armorEffects.clear();
        ArmorEffects.armorSetsEffects.clear();
    }
}