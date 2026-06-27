package com.zeydie.slotseffect.mountcore;

import com.zeydie.slotseffect.bukkit.listeners.EntityListener;
import com.zeydie.slotseffect.bukkit.listeners.PlayerListener;
import com.zeydie.slotseffect.bukkit.tasks.InventoryTask;
import com.zeydie.slotseffect.mountcore.modules.GsonConfigurationPluginModule;
import lombok.Getter;
import net.kyori.adventure.text.Component;
import org.bukkit.plugin.PluginManager;
import org.jetbrains.annotations.NotNull;
import ru.mountcode.plugins.mountcore.api.v3.bukkit.Sender;
import ru.mountcode.plugins.mountcore.paper.api.bootstrap.v1.MountPlugin;

public final class SlotsEffect extends MountPlugin {
    private static final @NotNull String ID = "slotseffect";

    @Getter
    private static SlotsEffect instance;

    private final @NotNull PluginManager pluginManager = this.getServer().getPluginManager();

    @Getter
    private GsonConfigurationPluginModule configurationModule;
    //private final ConfigurationPluginModule configurationModule = new ConfigurationPluginModule(this);

    private final @NotNull EntityListener entityListener = new EntityListener();
    private final @NotNull PlayerListener playerListener = new PlayerListener();

    private final @NotNull InventoryTask inventoryTask = new InventoryTask();

    @Override
    public void construct() {
        this.getModuleManager().registerTranslationModule();
    }

    @Override
    public void enable() {
        instance = this;

        this.configurationModule = new GsonConfigurationPluginModule(this);

        this.getModuleManager().registerModule(this.configurationModule);

        this.pluginManager.registerEvents(this.entityListener, this);
        this.pluginManager.registerEvents(this.playerListener, this);

        this.inventoryTask.runTaskTimer(this, 0, 20);

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