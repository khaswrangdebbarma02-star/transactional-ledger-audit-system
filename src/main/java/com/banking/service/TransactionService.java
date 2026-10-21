
package com.banking.service;

import com.banking.model.Account;
import com.banking.model.LedgerEntry;
import com.banking.model.Transaction;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TransactionService {

    private final List<Transaction> transactionHistory =
            new ArrayList<>();

    private final List<LedgerEntry> ledgerEntries =
            new ArrayList<>();

    private int nextTransactionId = 1;
    private int nextLedgerEntryId = 1;

    public synchronized void transfer(
            Account from,
            Account to,
            BigDecimal amount) {

        if (from == null || to == null) {
            throw new IllegalArgumentException(
                "Accounts cannot be null");
        }

        if (from == to ||
                from.getAccountId() == to.getAccountId()) {
            throw new IllegalArgumentException(
                "Source and destination must be different accounts");
        }

        if (amount == null ||
                amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException(
                "Transfer amount must be greater than zero");
        }

        if (from.getBalance().compareTo(amount) < 0) {
            throw new IllegalArgumentException(
                "Insufficient balance for transfer");
        }

        from.withdraw(amount);
        to.deposit(amount);

        Transaction transaction = new Transaction(
            nextTransactionId++,
            from.getAccountId(),
            to.getAccountId(),
            amount,
            "SUCCESS"
        );

        transactionHistory.add(transaction);

        ledgerEntries.add(new LedgerEntry(
            nextLedgerEntryId++,
            transaction.getTransactionId(),
            from.getAccountId(),
            "DEBIT",
            amount
        ));

        ledgerEntries.add(new LedgerEntry(
            nextLedgerEntryId++,
            transaction.getTransactionId(),
            to.getAccountId(),
            "CREDIT",
            amount
        ));

        System.out.println(
            "Transfer successful: ₹" + amount
            + " from " + from.getAccountHolder()
            + " to " + to.getAccountHolder());
    }

    public synchronized List<Transaction> getTransactionHistory() {
        return Collections.unmodifiableList(
            new ArrayList<>(transactionHistory));
    }

    public synchronized List<LedgerEntry> getLedgerEntries() {
        return Collections.unmodifiableList(
            new ArrayList<>(ledgerEntries));
    }
}
