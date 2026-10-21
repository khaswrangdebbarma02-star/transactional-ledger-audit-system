
package com.banking.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Transaction {

    private final int transactionId;
    private final int fromAccountId;
    private final int toAccountId;
    private final BigDecimal amount;
    private final LocalDateTime timestamp;
    private final String status;

    public Transaction(
            int transactionId,
            int fromAccountId,
            int toAccountId,
            BigDecimal amount,
            String status) {

        this.transactionId = transactionId;
        this.fromAccountId = fromAccountId;
        this.toAccountId = toAccountId;
        this.amount = amount;
        this.status = status;
        this.timestamp = LocalDateTime.now();
    }

    public int getTransactionId() {
        return transactionId;
    }

    public int getFromAccountId() {
        return fromAccountId;
    }

    public int getToAccountId() {
        return toAccountId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public String getStatus() {
        return status;
    }
}
