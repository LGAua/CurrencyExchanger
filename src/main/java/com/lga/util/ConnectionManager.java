package com.lga.util;

import com.lga.exceptions.ConnectionFailureException;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.experimental.UtilityClass;

import java.sql.Connection;
import java.sql.SQLException;

@UtilityClass
public class ConnectionManager {
    private static final HikariDataSource dataSource;

    static {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(PropertiesUtil.getDataBasePath());
        config.setDriverClassName(PropertiesUtil.getDriverClassName());

        config.setMaximumPoolSize(10);
        config.setMaxLifetime(60000);
        dataSource = new HikariDataSource(config);
    }

    public static Connection open() {
        try {
            return dataSource.getConnection();
        } catch (SQLException e) {
            throw new ConnectionFailureException();
        }
    }
}
