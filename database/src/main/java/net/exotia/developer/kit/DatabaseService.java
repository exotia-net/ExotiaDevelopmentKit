package net.exotia.developer.kit;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.DataSourceConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import com.zaxxer.hikari.HikariDataSource;

import java.io.File;
import java.sql.SQLException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class DatabaseService {
    private final HikariDataSource dataSource;
    private ConnectionSource connectionSource;
    private final Map<Class<?>, Dao<?, ?>> cachedDao = new ConcurrentHashMap<>();
    private final DatabaseConfig config;
    private final File dataFolder;

    public DatabaseService(DatabaseConfig databaseConfig, File dataFolder) {
        this.config = databaseConfig;
        this.dataSource = new HikariDataSource();
        this.dataFolder = dataFolder;
    }

    public void connect() throws SQLException {
        this.dataSource.addDataSourceProperty("cachePrepStmts", true);
        this.dataSource.addDataSourceProperty("prepStmtCacheSize", 250);
        this.dataSource.addDataSourceProperty("prepStmtCacheSqlLimit", 2048);
        this.dataSource.addDataSourceProperty("useServerPrepStmts", true);

        this.dataSource.setMaximumPoolSize(5);
        this.dataSource.setUsername(this.config.getUsername());
        this.dataSource.setPassword(this.config.getPassword());

        switch (this.config.getDatabaseType()) {
            case MYSQL -> {
                this.dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
                this.dataSource.setJdbcUrl(this.getJdbc("jdbc:mysql"));
            }
            case MARIADB -> {
                this.dataSource.setDriverClassName("org.mariadb.jdbc.Driver");
                this.dataSource.setJdbcUrl(this.getJdbc("jdbc:mariadb"));
            }
            case H2 -> {
                this.dataSource.setDriverClassName("org.h2.Driver");
                this.dataSource.setJdbcUrl("jdbc:h2:./" + this.dataFolder + "/database");
            }
            case SQLITE -> {
                this.dataSource.setDriverClassName("org.sqlite.JDBC");
                this.dataSource.setJdbcUrl("jdbc:sqlite:" + this.dataFolder + "/database.db");
            }
            default -> throw new SQLException("SQL type '" + this.config.getDatabaseType() + "' not found");
        }
        this.connectionSource = new DataSourceConnectionSource(this.dataSource, this.dataSource.getJdbcUrl());
    }

    public void close() {
        try {
            this.dataSource.close();
            this.connectionSource.close();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    @SuppressWarnings("unchecked")
    public <T, ID> Dao<T, ID> getDao(Class<T> type) {
        try {
            Dao<?, ?> dao = this.cachedDao.get(type);

            if (dao == null) {
                dao = DaoManager.createDao(this.connectionSource, type);
                this.cachedDao.put(type, dao);
            }

            return (Dao<T, ID>) dao;
        } catch (SQLException exception) {
            throw new RuntimeException(exception);
        }
    }

    private String getJdbc(String driver) {
        return String.format("%s://%s:%s/%s", driver, this.config.getHostname(), this.config.getPort(), this.config.getDatabase());
    }

    public HikariDataSource getDataSource() {
        return dataSource;
    }

    public ConnectionSource getConnectionSource() {
        return connectionSource;
    }

    public Map<Class<?>, Dao<?, ?>> getCachedDao() {
        return cachedDao;
    }
}
