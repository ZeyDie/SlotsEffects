package com.zeydie.slotseffect.mountcore;

import com.zeydie.slotseffect.bukkit.listeners.EntityListener;
import com.zeydie.slotseffect.mountcore.modules.ConfigurationPluginModule;
import net.kyori.adventure.text.Component;
import org.bukkit.plugin.PluginManager;
import org.jetbrains.annotations.NotNull;
import ru.mountcode.plugins.mountcore.api.bootstrap.v1.module.PluginModuleManager;
import ru.mountcode.plugins.mountcore.api.v3.bukkit.Sender;
import ru.mountcode.plugins.mountcore.paper.api.bootstrap.v1.MountPlugin;

public final class SlotsEffect extends MountPlugin {
    private static final @NotNull String ID = "slotseffect";

    private final PluginManager pluginManager = this.getServer().getPluginManager();

    private final PluginModuleManager pluginModuleManager = this.getModuleManager();

    private final ConfigurationPluginModule configurationModule = new ConfigurationPluginModule(this);

    private final EntityListener playerListener = new EntityListener();

    @Override
    public void construct() {
        this.pluginModuleManager.registerTranslationModule();
    }

    @Override
    public void enable() {
        this.pluginModuleManager.registerModule(this.configurationModule);

        this.pluginManager.registerEvents(this.playerListener, this);

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