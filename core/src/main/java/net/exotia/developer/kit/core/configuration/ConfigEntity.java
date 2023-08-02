package net.exotia.developer.kit.core.configuration;

import eu.okaeri.configs.OkaeriConfig;

public class ConfigEntity {
    private String fileName;
    private Class<? extends OkaeriConfig> configClass;
    private OkaeriConfig config;

    public ConfigEntity(String fileName, Class<? extends OkaeriConfig> configClass, OkaeriConfig config) {
        this.fileName = fileName;
        this.configClass = configClass;
        this.config = config;
    }

    public String getFileName() {
        return fileName;
    }

    public Class<? extends OkaeriConfig> getConfigClass() {
        return configClass;
    }

    public OkaeriConfig getConfig() {
        return config;
    }
}
