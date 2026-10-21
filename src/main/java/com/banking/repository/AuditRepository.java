
package com.banking.repository;

import com.banking.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AuditRepository {

    public void verifySuccessfulTransactions() throws SQLException {
        String sql = """
                SELECT t.transaction_id
                FROM transactions t
                WHERE t.status = 'SUCCESS'
                  AND (
                    (SELECT COUNT(*)
                     FROM ledger_entries l
                     WHERE l.transaction_id = t.transaction_id) <> 2

                    OR (SELECT COUNT(*)
                        FROM ledger_entries l
                        WHERE l.transaction_id = t.transaction_id
                          AND l.account_id = t.from_account_id
                          AND l.entry_type = 'DEBIT'
                          AND l.amount = t.amount) <> 1

                    OR (SELECT COUNT(*)
                        FROM ledger_entries l
                        WHERE l.transaction_id = t.transaction_id
                          AND l.account_id = t.to_account_id
                          AND l.entry_type = 'CREDIT'
                          AND l.amount = t.amount) <> 1
                  )
                ORDER BY t.transaction_id
                """;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql);
             ResultSet result = statement.executeQuery()) {

            boolean foundIssues = false;

            while (result.next()) {
                foundIssues = true;
                System.out.println(
                        "AUDIT ISSUE: Transaction "
                                + result.getLong("transaction_id")
                                + " has inconsistent ledger entries.");
            }

            if (!foundIssues) {
                System.out.println(
                        "AUDIT PASSED: All successful transactions "
                                + "have matching ledger entries.");
            }
        }
    }
}
