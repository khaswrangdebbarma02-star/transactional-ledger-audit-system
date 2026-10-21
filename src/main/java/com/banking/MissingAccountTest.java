
package com.banking;

import com.banking.repository.AccountRepository;
import com.banking.repository.TransferRepository;

import java.math.BigDecimal;

public class MissingAccountTest {

    public static void main(String[] args) throws Exception {
        AccountRepository accounts = new AccountRepository();
        TransferRepository transfers = new TransferRepository();

        BigDecimal beforeRahul = accounts.getBalance(101);

        try {
            transfers.transferMoney(
                    101,
                    999999,
                    new BigDecimal("100.00")
            );

            throw new AssertionError(
                    "Test failed: nonexistent account was accepted.");

        } catch (java.sql.SQLException e) {
            if (!"Source or destination account was not found."
                    .equals(e.getMessage())) {
                throw e;
            }

            System.out.println(
                    "PASS: Transfer to nonexistent account was rejected.");
        }

        BigDecimal afterRahul = accounts.getBalance(101);

        if (beforeRahul.compareTo(afterRahul) != 0) {
            throw new AssertionError(
                    "Test failed: Rahul's balance changed.");
        }

        System.out.println(
                "PASS: Rahul's balance remains unchanged.");
    }
}
