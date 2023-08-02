package net.exotia.developer.kit.core;

import net.exotia.developer.kit.core.configuration.ConfigEntity;

import java.util.ArrayList;
import java.util.List;

public class ExotiaPlugin {
    private final List<ConfigEntity> configs = new ArrayList<>();

    public List<ConfigEntity> getConfigs() {
        return configs;
    }

    public void reloadConfigs() {
        this.configs.forEach(config -> {
            config.getConfig().load(true);
        });
    }
}
