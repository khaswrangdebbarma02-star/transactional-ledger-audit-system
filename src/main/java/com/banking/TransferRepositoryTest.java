
package com.banking;

import com.banking.repository.TransferRepository;

import java.math.BigDecimal;

public class TransferRepositoryTest {

    public static void main(String[] args) {
        TransferRepository repository = new TransferRepository();

        try {
            long transactionId = repository.transferMoney(
                    101,
                    102,
                    new BigDecimal("1000.00")
            );

            System.out.println("Transfer completed successfully!");
            System.out.println("Transaction ID: " + transactionId);
            System.out.println("Amount transferred: ₹1000.00");
            System.out.println("From account: 101 (Rahul)");
            System.out.println("To account: 102 (Priya)");

        } catch (Exception e) {
            System.err.println("Transfer failed: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
