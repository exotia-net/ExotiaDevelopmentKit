package net.exotia.developer.kit;

public interface DatabaseConfig {
    DatabaseType getDatabaseType();
    String getHostname();
    int getPort();
    String getDatabase();
    String getUsername();
    String getPassword();
}
