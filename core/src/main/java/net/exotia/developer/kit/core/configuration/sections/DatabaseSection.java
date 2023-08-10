package net.exotia.developer.kit.core.configuration.sections;

import eu.okaeri.configs.OkaeriConfig;
import net.exotia.developer.kit.DatabaseConfig;
import net.exotia.developer.kit.DatabaseType;

public class DatabaseSection extends OkaeriConfig implements DatabaseConfig {
    public DatabaseType databaseType;
    public String hostname;
    private int port;
    private String database;
    private String username;
    private String password;

    public DatabaseSection(DatabaseType databaseType, String hostname, int port, String database, String username, String password) {
        this.databaseType = databaseType;
        this.hostname = hostname;
        this.port = port;
        this.database = database;
        this.username = username;
        this.password = password;
    }

    @Override
    public DatabaseType getDatabaseType() {
        return this.databaseType;
    }

    @Override
    public String getHostname() {
        return this.hostname;
    }

    @Override
    public int getPort() {
        return this.port;
    }

    @Override
    public String getDatabase() {
        return this.database;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    @Override
    public String getPassword() {
        return this.password;
    }
}
