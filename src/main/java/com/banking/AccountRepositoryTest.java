package com.banking;

import com.banking.model.Account;
import com.banking.repository.AccountRepository;

import java.math.BigDecimal;

public class AccountRepositoryTest {

    public static void main(String[] args) {
        AccountRepository repository = new AccountRepository();

        try {
            // Create two sample accounts.
            Account account1 = new Account(
                    101,
                    "Rahul",
                    new BigDecimal("10000.00")
            );

            Account account2 = new Account(
                    102,
                    "Priya",
                    new BigDecimal("5000.00")
            );

            // Save both accounts to Oracle Database.
            repository.createAccount(account1);
            repository.createAccount(account2);

            System.out.println("Both accounts inserted successfully!");

            // Retrieve and display the saved accounts.
            Account savedAccount1 = repository.findById(101);
            Account savedAccount2 = repository.findById(102);

            displayAccount(savedAccount1);
            displayAccount(savedAccount2);

        } catch (Exception e) {
            System.err.println("Account repository test failed: "
                    + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void displayAccount(Account account) {
        if (account != null) {
            System.out.println("\nAccount ID: "
                    + account.getAccountId());
            System.out.println("Account Holder: "
                    + account.getAccountHolder());
            System.out.println("Balance: ₹"
                    + account.getBalance());
        }
    }
}