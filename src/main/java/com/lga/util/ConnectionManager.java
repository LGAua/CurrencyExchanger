package com.lga.util;

import com.lga.exceptions.ConnectionFailureException;
import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@UtilityClass
public class ConnectionManager {
    private static final String URL = "jdbc:sqlite:C:/Users/glebl/OneDrive/Desktop/CurrencyExchanger-master/src/main/resources/database/CurrencyExchangerDataBase.db";

    static {
        loadDriver();
    }

    public static Connection open() {
        try {
            return DriverManager.getConnection(URL);
        } catch (SQLException e) {
            throw new ConnectionFailureException();
        }
    }

    @SneakyThrows
    private static void loadDriver() {
        Class.forName("org.sqlite.JDBC");
    }
}
