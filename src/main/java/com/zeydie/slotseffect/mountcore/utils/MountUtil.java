package com.zeydie.slotseffect.mountcore.utils;

import com.zeydie.slotseffect.mountcore.SlotsEffect;
import lombok.NonNull;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import ru.mountcode.plugins.mountcore.api.bootstrap.v1.module.PluginModule;

import java.nio.file.Path;

public final class MountUtil {
    public static @NotNull Logger getLogger(@NonNull final PluginModule module) {
        return module.getPlugin().logger();
    }

    public static @NotNull Path getPluginDirectory(@NonNull final PluginModule module) {
        return SlotsEffect.getInstance().getPluginDirectory();//return module.getPlugin().getPluginDirectory();
    }
}