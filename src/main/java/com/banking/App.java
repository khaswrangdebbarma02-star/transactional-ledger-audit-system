
package com.banking;

import com.banking.model.Account;
import com.banking.model.Transaction;
import com.banking.model.LedgerEntry;
import com.banking.service.TransactionService;

import java.math.BigDecimal;

public class App {

    public static void main(String[] args) {

        Account rahul = new Account(
            101,
            "Rahul",
            new BigDecimal("10000.00")
        );

        Account priya = new Account(
            102,
            "Priya",
            new BigDecimal("5000.00")
        );

        TransactionService transactionService =
            new TransactionService();

        System.out.println("=== Before Transfer ===");
        System.out.println(
            "Rahul's Balance: ₹" + rahul.getBalance());
        System.out.println(
            "Priya's Balance: ₹" + priya.getBalance());

        System.out.println("\n=== Processing Transfer ===");

        transactionService.transfer(
            rahul,
            priya,
            new BigDecimal("2000.00")
        );

        System.out.println("\n=== After Transfer ===");
        System.out.println(
            "Rahul's Balance: ₹" + rahul.getBalance());
        System.out.println(
            "Priya's Balance: ₹" + priya.getBalance());

        System.out.println("\n=== Transaction History ===");

        for (Transaction transaction :
                transactionService.getTransactionHistory()) {

            System.out.println(
                "Transaction ID: " + transaction.getTransactionId()
                + " | From Account: " + transaction.getFromAccountId()
                + " | To Account: " + transaction.getToAccountId()
                + " | Amount: ₹" + transaction.getAmount()
                + " | Status: " + transaction.getStatus()
                + " | Time: " + transaction.getTimestamp()
            );
        }

        System.out.println("\n=== Double-Entry Ledger ===");

        for (LedgerEntry entry :
                transactionService.getLedgerEntries()) {

            System.out.println(
                "Entry ID: " + entry.getEntryId()
                + " | Transaction ID: " + entry.getTransactionId()
                + " | Account ID: " + entry.getAccountId()
                + " | Type: " + entry.getEntryType()
                + " | Amount: ₹" + entry.getAmount()
                + " | Time: " + entry.getTimestamp()
            );
        }
    }
}
