package com.zeydie.slotseffect.mountcore;

import com.zeydie.slotseffect.bukkit.listeners.EntityListener;
import com.zeydie.slotseffect.bukkit.tasks.InventoryTask;
import com.zeydie.slotseffect.mountcore.modules.GsonConfigurationPluginModule;
import lombok.Getter;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.jetbrains.annotations.NotNull;
import ru.mountcode.plugins.mountcore.api.v3.bukkit.Sender;
import ru.mountcode.plugins.mountcore.paper.api.bootstrap.v1.MountPlugin;

public final class SlotsEffect extends MountPlugin {
    private static final @NotNull String ID = "slotseffect";

    @Getter
    private static SlotsEffect instance;

    private final PluginManager pluginManager = this.getServer().getPluginManager();

    @Getter
    private GsonConfigurationPluginModule configurationModule;
    //private final ConfigurationPluginModule configurationModule = new ConfigurationPluginModule(this);

    private final EntityListener playerListener = new EntityListener();

    @Override
    public void construct() {
        this.getModuleManager().registerTranslationModule();
    }

    @Override
    public void enable() {
        instance = this;

        this.configurationModule = new GsonConfigurationPluginModule(this);

        this.getModuleManager().registerModule(this.configurationModule);

        this.pluginManager.registerEvents(this.playerListener, this);

        Bukkit.getScheduler()
                .runTaskTimer(
                        this,
                        new InventoryTask(),
                        0,
                        20
                );

        Sender.sendConsole(Component.translatable(ID + ".enabled"));
    }

    @Override
    public void shutdown() {
        Sender.sendConsole(Component.translatable(ID + ".disabled"));
    }

    @Override
    public void reload() {
    }
}