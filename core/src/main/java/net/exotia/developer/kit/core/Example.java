package net.exotia.developer.kit.core;

import net.exotia.developer.kit.core.commands.CommandsFactory;

public class Example {
    public void onEnable() {
        PluginFactory.create(null)
                .listener()
                .liteCommands(null, builder -> {})
                .bootstrap();
    }
}
