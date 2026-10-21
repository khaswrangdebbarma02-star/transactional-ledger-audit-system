
package com.banking.repository;

import com.banking.DatabaseConnection;
import com.banking.model.Account;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AccountRepository {

    public void createAccount(Account account) throws SQLException {
        String sql = """
                INSERT INTO accounts
                    (account_id, account_holder, balance)
                VALUES (?, ?, ?)
                """;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, account.getAccountId());
            statement.setString(2, account.getAccountHolder());
            statement.setBigDecimal(3, account.getBalance());

            statement.executeUpdate();
        }
    }

    public Account findById(int accountId) throws SQLException {
        String sql = """
                SELECT account_id, account_holder, balance
                FROM accounts
                WHERE account_id = ?
                """;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, accountId);

            try (ResultSet result = statement.executeQuery()) {
                if (result.next()) {
                    return new Account(
                            result.getInt("account_id"),
                            result.getString("account_holder"),
                            result.getBigDecimal("balance")
                    );
                }
            }
        }

        return null;
    }

    public BigDecimal getBalance(int accountId) throws SQLException {
        String sql = "SELECT balance FROM accounts WHERE account_id = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, accountId);

            try (ResultSet result = statement.executeQuery()) {
                if (result.next()) {
                    return result.getBigDecimal("balance");
                }
            }
        }

        return null;
    }
}
