
package com.banking;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import com.banking.model.Account;
import com.banking.service.TransactionService;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class TransactionServiceTest {

    @Test
    void transferShouldUpdateBothBalances() {

        Account rahul = new Account(
            101, "Rahul", new BigDecimal("10000.00"));

        Account priya = new Account(
            102, "Priya", new BigDecimal("5000.00"));

        TransactionService service =
            new TransactionService();

        service.transfer(
            rahul, priya, new BigDecimal("2000.00"));

        assertEquals(
            0,
            rahul.getBalance().compareTo(
                new BigDecimal("8000.00")));

        assertEquals(
            0,
            priya.getBalance().compareTo(
                new BigDecimal("7000.00")));

        assertEquals(1, service.getTransactionHistory().size());
    }

    @Test
    void transferShouldRejectInsufficientBalance() {

        Account rahul = new Account(
            101, "Rahul", new BigDecimal("1000.00"));

        Account priya = new Account(
            102, "Priya", new BigDecimal("5000.00"));

        TransactionService service =
            new TransactionService();

        assertThrows(
            IllegalArgumentException.class,
            () -> service.transfer(
                rahul, priya, new BigDecimal("2000.00"))
        );

        assertEquals(
            0,
            rahul.getBalance().compareTo(
                new BigDecimal("1000.00")));

        assertEquals(
            0,
            priya.getBalance().compareTo(
                new BigDecimal("5000.00")));

        assertTrue(service.getTransactionHistory().isEmpty());
    }

    @Test
    void concurrentTransfersShouldPreserveTotalBalance()
            throws InterruptedException {

        Account rahul = new Account(
            101, "Rahul", new BigDecimal("10000.00"));

        Account priya = new Account(
            102, "Priya", new BigDecimal("10000.00"));

        TransactionService service =
            new TransactionService();

        ExecutorService executor =
            Executors.newFixedThreadPool(4);

        for (int i = 0; i < 20; i++) {
            executor.submit(() -> service.transfer(
                rahul, priya, new BigDecimal("10.00")));
        }

        executor.shutdown();

        assertTrue(
            executor.awaitTermination(10, TimeUnit.SECONDS),
            "Transfers did not finish in time");

        assertEquals(
            0,
            rahul.getBalance()
                .add(priya.getBalance())
                .compareTo(new BigDecimal("20000.00")));

        assertEquals(20, service.getTransactionHistory().size());

        assertEquals(
            0,
            rahul.getBalance().compareTo(
                new BigDecimal("9800.00")));

        assertEquals(
            0,
            priya.getBalance().compareTo(
                new BigDecimal("10200.00")));
            }

    @Test
    void successfulTransferShouldCreateBalancedLedgerEntries() {

        Account rahul = new Account(
            101, "Rahul", new BigDecimal("10000.00"));

        Account priya = new Account(
            102, "Priya", new BigDecimal("5000.00"));

        TransactionService service =
            new TransactionService();

        service.transfer(
            rahul, priya, new BigDecimal("2000.00"));

        var entries = service.getLedgerEntries();

        assertEquals(2, entries.size());

        assertEquals("DEBIT", entries.get(0).getEntryType());
        assertEquals("CREDIT", entries.get(1).getEntryType());

        assertEquals(
            0,
            entries.get(0).getAmount().compareTo(
                entries.get(1).getAmount()));

        assertEquals(
            entries.get(0).getTransactionId(),
            entries.get(1).getTransactionId());
    }

    

}
