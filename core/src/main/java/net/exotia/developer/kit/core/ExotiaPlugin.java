package net.exotia.developer.kit.core;

import net.exotia.developer.kit.core.configuration.ConfigEntity;
import net.exotia.developer.kit.core.exceptions.InjectorIsNotCreated;
import net.exotia.developer.kit.injector.Injector;

import java.util.ArrayList;
import java.util.List;

public class ExotiaPlugin {
    private Injector injector = null;
    private final List<ConfigEntity> configs = new ArrayList<>();

    public List<ConfigEntity> getConfigs() {
        return configs;
    }

    public void reloadConfigs() {
        this.configs.forEach(config -> {
            config.getConfig().load(true);
        });
    }

    public Injector getInjector() {
        if (this.injector == null) throw new InjectorIsNotCreated();
        return injector;
    }

    public void setInjector(Injector injector) {
        this.injector = injector;
    }
}
