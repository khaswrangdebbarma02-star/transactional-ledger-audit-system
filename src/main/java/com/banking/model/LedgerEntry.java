
package com.banking.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class LedgerEntry {

    private final int entryId;
    private final int transactionId;
    private final int accountId;
    private final String entryType;
    private final BigDecimal amount;
    private final LocalDateTime timestamp;

    public LedgerEntry(
            int entryId,
            int transactionId,
            int accountId,
            String entryType,
            BigDecimal amount) {

        this.entryId = entryId;
        this.transactionId = transactionId;
        this.accountId = accountId;
        this.entryType = entryType;
        this.amount = amount;
        this.timestamp = LocalDateTime.now();
    }

    public int getEntryId() {
        return entryId;
    }

    public int getTransactionId() {
        return transactionId;
    }

    public int getAccountId() {
        return accountId;
    }

    public String getEntryType() {
        return entryType;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }
}
