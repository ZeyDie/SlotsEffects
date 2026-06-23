package com.zeydie.slotseffect.mountcore.modules;

import com.google.common.collect.Maps;
import com.zeydie.slotseffect.bukkit.utils.BukkitUtil;
import com.zeydie.slotseffect.mountcore.SlotsEffect;
import com.zeydie.slotseffect.bukkit.utils.Constructors;
import com.zeydie.slotseffect.mountcore.utils.MountUtil;
import lombok.Getter;
import lombok.NonNull;
import lombok.val;
import org.jetbrains.annotations.NotNull;
import ru.mountcode.plugins.mountcore.api.bootstrap.v1.module.IReloadableModule;
import ru.mountcode.plugins.mountcore.api.bootstrap.v1.module.PluginModule;
import ru.mountcode.plugins.mountcore.api.configuration.v1.exception.InvalidConfigurationException;
import ru.mountcode.plugins.mountcore.api.configuration.v1.impl.yaml.loader.YamlConfigurationLoader;

import java.io.IOException;
import java.util.Arrays;
import java.util.Map;

public class ConfigurationPluginModule extends PluginModule implements IReloadableModule {
    private final YamlConfigurationLoader configLoader;
    private final YamlConfigurationLoader effectsLoader;

    @Getter
    private final @NotNull String name = "config";
    @Getter
    private final @NotNull Map<String, Boolean> effects = Maps.newHashMap();

    public ConfigurationPluginModule(final SlotsEffect plugin) {
        super(plugin);

        @NonNull val directory = MountUtil.getPluginDirectory(this);

        this.configLoader = new YamlConfigurationLoader(
                directory.resolve("config.yml"),
                ConfigurationPluginModule.class.getResource("/config.yml")
        );
        this.effectsLoader = new YamlConfigurationLoader(
                directory.resolve("effects.yml"),
                ConfigurationPluginModule.class.getResource("/effects.yml")
        );
    }

    @Override
    public void enable() {
        try {
            this.initialize();
        } catch (final IOException | InvalidConfigurationException e) {
            throw new RuntimeException("Error on initializing configuration ", e);
        }
    }

    @Override
    public void shutdown() {

    }

    @Override
    public boolean reload() {
        try {
            this.initialize();
            return true;
        } catch (final IOException | InvalidConfigurationException e) {
            MountUtil.getLogger(this).error("Error on reloading configuration ", e);
            return false;
        }
    }


    private void initialize() throws IOException, InvalidConfigurationException {
        this.initConfig();
        this.initEffects();
    }

    private void initConfig() throws InvalidConfigurationException, IOException {
        @NonNull val configuration = this.configLoader.load();
        @NonNull val effectsSection = configuration.getConfigurationSection("effects");

        if (effectsSection == null)
            throw new InvalidConfigurationException("Section effects not found in config.yml");
    }

    private void initEffects() throws InvalidConfigurationException, IOException {
        @NonNull val configuration = this.effectsLoader.load();
        @NonNull val effectsSection = configuration.getConfigurationSection("effects");

        if (effectsSection == null) {
            MountUtil.getLogger(this).error("Section effects not found in effects.yml");

            BukkitUtil.getEffects().forEach(type -> effectsSection.set(Constructors.getPathOfEffect(type), true));

            configuration.set("effects", effectsSection);
        }

        BukkitUtil.getEffects()
                .forEach(
                        type -> {
                            @NonNull val path = Constructors.getPathOfEffect(type);
                            val enabled = effectsSection.getBoolean(path, true);

                            this.effects.put(path, enabled);
                        }
                );

        this.effectsLoader.save(configuration);
    }
}