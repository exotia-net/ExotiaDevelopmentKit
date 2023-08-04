package net.exotia.developer.kit.core;

import dev.rollczi.litecommands.LiteCommandsBuilder;
import net.exotia.developer.kit.core.configuration.ConfigEntity;
import net.exotia.developer.kit.core.exceptions.InjectorIsNotCreated;
import net.exotia.developer.kit.injector.Injector;
import org.bukkit.Server;
import org.bukkit.command.CommandSender;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.List;

public class ExotiaPlugin {
    private Injector injector = null;
    private final Plugin plugin;
    private LiteCommandsBuilder<CommandSender> liteCommandsBuilder;
    private final List<Listener> listeners = new ArrayList<>();
    private final List<ConfigEntity> configs = new ArrayList<>();

    public ExotiaPlugin(Plugin plugin) {
        this.plugin = plugin;
    }

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

    public LiteCommandsBuilder<CommandSender> getLiteCommandsBuilder() {
        return liteCommandsBuilder;
    }

    public void setLiteCommandsBuilder(LiteCommandsBuilder<CommandSender> liteCommandsBuilder) {
        this.liteCommandsBuilder = liteCommandsBuilder;
    }

    public List<Listener> getListeners() {
        return listeners;
    }

    public void bootstrap() {
        this.listeners.forEach(listener -> this.plugin.getServer().getPluginManager().registerEvents(listener, this.plugin));
        this.liteCommandsBuilder.register();
    }
}
