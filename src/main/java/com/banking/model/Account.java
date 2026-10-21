
package com.banking.model;

import java.math.BigDecimal;

public class Account {

    private final int accountId;
    private final String accountHolder;
    private BigDecimal balance;

    public Account(int accountId, String accountHolder,
                   BigDecimal initialBalance) {

        if (accountId <= 0) {
            throw new IllegalArgumentException(
                "Account ID must be positive");
        }

        if (accountHolder == null || accountHolder.isBlank()) {
            throw new IllegalArgumentException(
                "Account holder name cannot be empty");
        }

        if (initialBalance == null ||
                initialBalance.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException(
                "Initial balance cannot be negative");
        }

        this.accountId = accountId;
        this.accountHolder = accountHolder;
        this.balance = initialBalance;
    }

    public int getAccountId() {
        return accountId;
    }

    public String getAccountHolder() {
        return accountHolder;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void deposit(BigDecimal amount) {
        validateAmount(amount);
        balance = balance.add(amount);
    }

    public void withdraw(BigDecimal amount) {
        validateAmount(amount);

        if (balance.compareTo(amount) < 0) {
            throw new IllegalArgumentException(
                "Insufficient balance");
        }

        balance = balance.subtract(amount);
    }

    private void validateAmount(BigDecimal amount) {
        if (amount == null ||
                amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException(
                "Amount must be greater than zero");
        }
    }
}
