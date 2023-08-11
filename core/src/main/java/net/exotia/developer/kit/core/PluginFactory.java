package net.exotia.developer.kit.core;

import com.j256.ormlite.table.TableUtils;
import com.zaxxer.hikari.HikariDataSource;
import dev.rollczi.litecommands.LiteCommandsBuilder;
import eu.okaeri.configs.OkaeriConfig;
import net.exotia.developer.kit.DatabaseService;
import net.exotia.developer.kit.DatabaseType;
import net.exotia.developer.kit.Scheduler;
import net.exotia.developer.kit.core.actions.Action;
import net.exotia.developer.kit.core.actions.ActionService;
import net.exotia.developer.kit.core.commands.CommandsFactory;
import net.exotia.developer.kit.core.configuration.ConfigEntity;
import net.exotia.developer.kit.core.configuration.ConfigurationFactory;
import net.exotia.developer.kit.core.configuration.sections.DatabaseSection;
import net.exotia.developer.kit.core.configuration.sections.LiteCommandsSection;
import net.exotia.developer.kit.core.exceptions.InjectorIsNotCreated;
import net.exotia.developer.kit.core.scheduler.SchedulerBukkit;
import net.exotia.developer.kit.injector.Injector;
import net.exotia.developer.kit.injector.InjectorSource;
import net.exotia.developer.kit.repositories.AbstractRepository;
import org.bukkit.command.CommandSender;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.function.Consumer;

public class PluginFactory {
    private final Plugin plugin;
    private final ConfigurationFactory configurationFactory;
    private final ExotiaPlugin exotiaPlugin;

    public PluginFactory(Plugin plugin) {
        this.plugin = plugin;
        this.exotiaPlugin = new ExotiaPlugin(plugin);
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
        this.exotiaPlugin.getListeners().addAll(Arrays.asList(listeners));
        return this;
    }

    public <T extends OkaeriConfig> PluginFactory configFile(Class<T> configClass, String fileName, Consumer<T> config) {
        T product = this.configurationFactory.produce(configClass, fileName);
        ConfigEntity configEntity = new ConfigEntity(fileName, configClass, product);
        this.exotiaPlugin.getConfigs().add(configEntity);
        if (this.exotiaPlugin.getInjector() != null) {
            this.exotiaPlugin.getInjector().registerInjectable("config/"+fileName, product);
        }
        config.accept(product);
        return this;
    }

    public PluginFactory liteCommands(LiteCommandsSection messages, Consumer<LiteCommandsBuilder<CommandSender>> consumer) {
        this.exotiaPlugin.setLiteCommandsBuilder(CommandsFactory.create(this.plugin, messages));
        consumer.accept(this.exotiaPlugin.getLiteCommandsBuilder());
        return this;
    }
    public PluginFactory useScheduler(boolean registerAsInjectable) {
        Scheduler scheduler = new SchedulerBukkit(this.plugin);
        if (registerAsInjectable && this.exotiaPlugin.getInjector() != null) this.injectable(scheduler);
        this.exotiaPlugin.setScheduler(scheduler);
        return this;
    }
    public PluginFactory repository(Class<?> dao, AbstractRepository abstractRepository) {
        try {
            TableUtils.createTableIfNotExists(this.exotiaPlugin.getDatabaseService().getConnectionSource(), dao);
            this.exotiaPlugin.getInjector().registerInjectable(abstractRepository);
        } catch (SQLException sqlException) {
            throw new RuntimeException(sqlException);
        }
        return this;
    }
    public PluginFactory useDatabase(DatabaseSection databaseSection, Consumer<HikariDataSource> dataSource) {
        DatabaseService databaseService = new DatabaseService(databaseSection, this.plugin.getDataFolder());
        dataSource.accept(databaseService.getDataSource());
        this.exotiaPlugin.setDatabaseService(databaseService);
        try {
            databaseService.connect();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return this;
    }

    public PluginFactory useActions() {
        this.exotiaPlugin.setActionService(new ActionService());
        return this;
    }
    public PluginFactory actions(Action... actions) {
        for (Action action : actions) {
            this.exotiaPlugin.getActionService().register(action);
        }
        return this;
    }
    public PluginFactory actions(Class<? extends Action>... actions) {
        for (Class<? extends Action> actionClass : actions) {
            Action action = this.exotiaPlugin.getInjector().createInstance(actionClass);
            this.actions(action);
        }
        return this;
    }
    public ExotiaPlugin produce() {
        return this.exotiaPlugin;
    }
}
