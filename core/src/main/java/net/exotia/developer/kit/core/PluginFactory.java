package net.exotia.developer.kit.core;

import dev.rollczi.litecommands.LiteCommandsBuilder;
import eu.okaeri.configs.OkaeriConfig;
import net.exotia.developer.kit.core.commands.CommandsFactory;
import net.exotia.developer.kit.core.configuration.ConfigEntity;
import net.exotia.developer.kit.core.configuration.ConfigurationFactory;
import net.exotia.developer.kit.core.configuration.sections.LiteCommandsSection;
import net.exotia.developer.kit.core.exceptions.InjectorIsNotCreated;
import net.exotia.developer.kit.injector.Injector;
import net.exotia.developer.kit.injector.InjectorSource;
import org.bukkit.Server;
import org.bukkit.command.CommandSender;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

public class PluginFactory {
    private final Server server;
    private final Plugin plugin;
    private LiteCommandsBuilder<CommandSender> liteCommandsBuilder;
    private final List<Listener> listeners = new ArrayList<>();
    private final ConfigurationFactory configurationFactory;
    private final ExotiaPlugin exotiaPlugin = new ExotiaPlugin();

    public PluginFactory(Plugin plugin) {
        this.plugin = plugin;
        this.server = plugin.getServer();
        this.configurationFactory = new ConfigurationFactory(plugin.getDataFolder());
    }

    public static PluginFactory create(Plugin plugin) {
        return new PluginFactory(plugin);
    }

    public PluginFactory useInjector() {
        Injector injector = InjectorSource.create();
        injector.registerInjectable(injector);
        this.exotiaPlugin.setInjector(injector);
        return this;
    }
    public PluginFactory injectable(Object... objects) {
        if (this.exotiaPlugin.getInjector() == null) throw new InjectorIsNotCreated();
        for (Object object : objects) {
            this.exotiaPlugin.getInjector().registerInjectable(object);
        }
        return this;
    }

    public PluginFactory listeners(Listener... listeners) {
        this.listeners.addAll(Arrays.asList(listeners));
        return this;
    }

    public <T extends OkaeriConfig> PluginFactory configFile(Class<T> configClass, String fileName) {
        ConfigEntity config = new ConfigEntity(fileName, configClass, this.configurationFactory.produce(configClass, fileName));
        this.exotiaPlugin.getConfigs().add(config);
        if (this.exotiaPlugin.getInjector() != null) {
            this.exotiaPlugin.getInjector().registerInjectable("config/"+fileName, config.getConfig());
        }
        return this;
    }

    public PluginFactory liteCommands(LiteCommandsSection messages, Consumer<LiteCommandsBuilder<CommandSender>> consumer) {
        this.liteCommandsBuilder = CommandsFactory.create(this.plugin, messages);
        consumer.accept(this.liteCommandsBuilder);
        return this;
    }

    public ExotiaPlugin bootstrap() {
        this.listeners.forEach(listener -> this.server.getPluginManager().registerEvents(listener, this.plugin));
        this.liteCommandsBuilder.register();
        return this.exotiaPlugin;
    }
}
