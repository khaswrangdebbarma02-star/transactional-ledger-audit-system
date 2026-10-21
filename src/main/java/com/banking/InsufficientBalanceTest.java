
package com.banking;

import com.banking.repository.AccountRepository;
import com.banking.repository.TransferRepository;

import java.math.BigDecimal;

public class InsufficientBalanceTest {

    public static void main(String[] args) throws Exception {
        AccountRepository accounts = new AccountRepository();
        TransferRepository transfers = new TransferRepository();

        BigDecimal beforeRahul = accounts.getBalance(101);
        BigDecimal beforePriya = accounts.getBalance(102);

        try {
            transfers.transferMoney(
                    101,
                    102,
                    new BigDecimal("999999999.00")
            );

            throw new AssertionError(
                    "Test failed: transfer should have been rejected.");

        } catch (IllegalArgumentException e) {
            if (!"Insufficient balance.".equals(e.getMessage())) {
                throw e;
            }
            System.out.println(
                    "PASS: Insufficient-balance transfer was rejected.");
        }

        BigDecimal afterRahul = accounts.getBalance(101);
        BigDecimal afterPriya = accounts.getBalance(102);

        if (beforeRahul.compareTo(afterRahul) != 0
                || beforePriya.compareTo(afterPriya) != 0) {
            throw new AssertionError(
                    "Test failed: account balances changed.");
        }

        System.out.println(
                "PASS: Both account balances remain unchanged.");
    }
}
