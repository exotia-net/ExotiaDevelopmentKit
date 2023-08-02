package net.exotia.developer.kit.core.configuration;

import eu.okaeri.configs.ConfigManager;
import eu.okaeri.configs.OkaeriConfig;
import eu.okaeri.configs.postprocessor.SectionSeparator;
import eu.okaeri.configs.yaml.bukkit.YamlBukkitConfigurer;
import eu.okaeri.configs.yaml.bukkit.serdes.SerdesBukkit;

import java.io.File;

public class ConfigurationFactory {
    private final File defaultDir;

    public ConfigurationFactory(File defaultDir) {
        this.defaultDir = defaultDir;
    }

    public <T extends OkaeriConfig> T produce(Class<T> clazz, String fileName) {
        return this.produce(clazz, new File(this.defaultDir, fileName));
    }

    public <T extends OkaeriConfig> T produce(Class<T> clazz, File file) {
        return ConfigManager.create(clazz, it-> {
            it.withConfigurer(new YamlBukkitConfigurer("#", SectionSeparator.NEW_LINE))
                    .withBindFile(file)
                    .withSerdesPack(new SerdesBukkit())
                    .saveDefaults().load(true);
        });
    }
}