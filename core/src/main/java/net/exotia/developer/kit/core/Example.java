package net.exotia.developer.kit.core;

import net.exotia.developer.kit.core.commands.CommandsFactory;

public class Example {
    private PluginConfiguration pluginConfiguration;
    public void onEnable() {
        ExotiaPlugin exotiaPlugin = PluginFactory.create(null)
                .useInjector()
                .injectable()
                .listeners()
                .configFile(PluginConfiguration.class, "configuration.yml", pluginConfiguration -> this.pluginConfiguration = pluginConfiguration)
                .liteCommands(null, builder -> {})
                .useDatabase(null, hikariDataSource -> {})
                .produce();

        exotiaPlugin.bootstrap();
    }
}
