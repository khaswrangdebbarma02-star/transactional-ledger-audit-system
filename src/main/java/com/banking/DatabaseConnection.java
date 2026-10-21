
package com.banking;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

    private static final String URL =
            "jdbc:oracle:thin:@//localhost:1521/FREEPDB1";

    private static final String USER = "ledger_app";

    public static Connection getConnection() throws SQLException {
        String password = System.getenv("DB_PASSWORD");

        if (password == null || password.isBlank()) {
            throw new IllegalStateException(
                    "Please set the DB_PASSWORD environment variable.");
        }

        return DriverManager.getConnection(URL, USER, password);
    }

    public static void main(String[] args) {
        try (Connection connection = getConnection()) {
            System.out.println(
                    "Connected to Oracle Database successfully!");
            System.out.println(
                    "Database connection is valid: "
                    + connection.isValid(5));
        } catch (Exception e) {
            System.err.println(
                    "Database connection failed: " + e.getMessage());
        }
    }
}
