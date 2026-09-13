package com.beyondthefeed.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public final class DatabaseConnection {
    private static final String URL = getValue("DB_URL", "jdbc:mysql://localhost:3306/beyond_the_feed?useSSL=false&serverTimezone=UTC");
    private static final String USER = getValue("DB_USER", "root");
    private static final String PASSWORD = getValue("DB_PASSWORD", "");

    private DatabaseConnection() { }

    public static Connection open() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    private static String getValue(String key, String fallback) {
        String value = System.getenv(key);
        return value == null || value.trim().isEmpty() ? fallback : value;
    }
}
