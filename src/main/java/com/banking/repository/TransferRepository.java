
package com.banking.repository;

import com.banking.DatabaseConnection;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;

public class TransferRepository {

    private final TransactionRepository transactionRepository =
            new TransactionRepository();

    public long transferMoney(
            int fromAccountId,
            int toAccountId,
            BigDecimal amount
    ) throws SQLException {

        if (fromAccountId == toAccountId) {
            throw new IllegalArgumentException(
                    "Source and destination accounts must be different.");
        }

        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException(
                    "Transfer amount must be greater than zero.");
        }

        try (Connection connection = DatabaseConnection.getConnection()) {
            connection.setAutoCommit(false);

            try {
                // Lock accounts in ascending ID order.
                int firstId = Math.min(fromAccountId, toAccountId);
                int secondId = Math.max(fromAccountId, toAccountId);

                BigDecimal firstBalance =
                        getBalanceForUpdate(connection, firstId);
                BigDecimal secondBalance =
                        getBalanceForUpdate(connection, secondId);

                if (firstBalance == null || secondBalance == null) {
                    throw new SQLException(
                            "Source or destination account was not found.");
                }

                BigDecimal fromBalance =
                        fromAccountId == firstId
                                ? firstBalance : secondBalance;

                BigDecimal toBalance =
                        toAccountId == firstId
                                ? firstBalance : secondBalance;

                if (fromBalance.compareTo(amount) < 0) {
                    throw new IllegalArgumentException(
                            "Insufficient balance.");
                }

                updateBalance(
                        connection,
                        fromAccountId,
                        fromBalance.subtract(amount)
                );

                updateBalance(
                        connection,
                        toAccountId,
                        toBalance.add(amount)
                );

                long transactionId =
                        transactionRepository.recordSuccessfulTransfer(
                                connection,
                                fromAccountId,
                                toAccountId,
                                amount
                        );

                connection.commit();
                return transactionId;

            } catch (SQLException | RuntimeException e) {
                try {
                    connection.rollback();
                } catch (SQLException rollbackError) {
                    e.addSuppressed(rollbackError);
                }
                throw e;
            }
        }
    }

    private BigDecimal getBalanceForUpdate(
            Connection connection,
            int accountId
    ) throws SQLException {

        String sql =
                "SELECT balance FROM accounts "
                + "WHERE account_id = ? FOR UPDATE";

        try (PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setInt(1, accountId);

            try (ResultSet result = statement.executeQuery()) {
                return result.next()
                        ? result.getBigDecimal("balance")
                        : null;
            }
        }
    }

    private void updateBalance(
            Connection connection,
            int accountId,
            BigDecimal newBalance
    ) throws SQLException {

        String sql =
                "UPDATE accounts SET balance = ? WHERE account_id = ?";

        try (PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setBigDecimal(1, newBalance);
            statement.setInt(2, accountId);

            if (statement.executeUpdate() != 1) {
                throw new SQLException(
                        "Failed to update account " + accountId);
            }
        }
    }
}
