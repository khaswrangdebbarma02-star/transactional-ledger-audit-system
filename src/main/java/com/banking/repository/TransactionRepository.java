
package com.banking.repository;

import com.banking.DatabaseConnection;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;

public class TransactionRepository {

    public long recordSuccessfulTransfer(
            Connection connection,
            int fromAccountId,
            int toAccountId,
            BigDecimal amount
    ) throws SQLException {

        String transactionSql = """
                INSERT INTO transactions
                    (from_account_id, to_account_id, amount, status, created_at)
                VALUES (?, ?, ?, ?, ?)
                """;

        long transactionId;

        try (PreparedStatement statement = connection.prepareStatement(
        transactionSql, new String[]{"TRANSACTION_ID"})) {

            statement.setInt(1, fromAccountId);
            statement.setInt(2, toAccountId);
            statement.setBigDecimal(3, amount);
            statement.setString(4, "SUCCESS");
            statement.setTimestamp(5, Timestamp.valueOf(LocalDateTime.now()));

            statement.executeUpdate();

            try (ResultSet keys = statement.getGeneratedKeys()) {
                if (!keys.next()) {
                    throw new SQLException(
                            "Could not retrieve transaction ID.");
                }
                transactionId = keys.getLong(1);
            }
        }

        insertLedgerEntry(
                connection, transactionId, fromAccountId, "DEBIT", amount);
        insertLedgerEntry(
                connection, transactionId, toAccountId, "CREDIT", amount);

        return transactionId;
    }

    private void insertLedgerEntry(
            Connection connection,
            long transactionId,
            int accountId,
            String entryType,
            BigDecimal amount
    ) throws SQLException {

        String sql = """
                INSERT INTO ledger_entries
                    (transaction_id, account_id, entry_type, amount, created_at)
                VALUES (?, ?, ?, ?, ?)
                """;

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, transactionId);
            statement.setInt(2, accountId);
            statement.setString(3, entryType);
            statement.setBigDecimal(4, amount);
            statement.setTimestamp(5, Timestamp.valueOf(LocalDateTime.now()));

            statement.executeUpdate();
        }
    }
}
