package net.exotia.developer.kit.core;

import net.exotia.developer.kit.core.commands.CommandsFactory;

public class Example {
    public void onEnable() {
        ExotiaPlugin exotiaPlugin = PluginFactory.create(null)
                .useInjector()
                .injectable()
                .listeners()
                .configFile(PluginConfiguration.class, "configuration.yml")
                .liteCommands(null, builder -> {})
                .bootstrap();

    }
}
