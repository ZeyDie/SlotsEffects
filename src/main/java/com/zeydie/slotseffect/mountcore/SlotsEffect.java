package com.zeydie.slotseffect.mountcore;

import com.zeydie.slotseffect.bukkit.listeners.EntityListener;
import com.zeydie.slotseffect.bukkit.listeners.PlayerListener;
import com.zeydie.slotseffect.bukkit.tasks.InventoryTask;
import com.zeydie.slotseffect.mountcore.modules.YmlConfigurationPluginModule;
import lombok.Getter;
import org.bukkit.plugin.PluginManager;
import org.jetbrains.annotations.NotNull;
import ru.mountcode.plugins.mountcore.paper.api.bootstrap.v1.MountPlugin;

public final class SlotsEffect extends MountPlugin {
    private static final @NotNull String ID = "slotseffect";

    private final @NotNull PluginManager pluginManager = this.getServer().getPluginManager();

    @Getter
    private YmlConfigurationPluginModule configurationModule;

    private final @NotNull EntityListener entityListener = new EntityListener();
    private final @NotNull PlayerListener playerListener = new PlayerListener();

    private final @NotNull InventoryTask inventoryTask = new InventoryTask();

    @Override
    public void construct() {
    }

    @Override
    public void enable() {
        this.configurationModule = new YmlConfigurationPluginModule(this);

        this.getModuleManager().registerModule(this.configurationModule);

        this.pluginManager.registerEvents(this.entityListener, this);
        this.pluginManager.registerEvents(this.playerListener, this);

        this.inventoryTask.runTaskTimer(this, 0, 20);
    }

    @Override
    public void shutdown() {

    }

    @Override
    public void reload() {

    }
}