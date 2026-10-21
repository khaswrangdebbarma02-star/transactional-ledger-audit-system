
package com.banking;

import com.banking.repository.AuditRepository;

public class AuditRepositoryTest {

    public static void main(String[] args) {
        AuditRepository auditRepository = new AuditRepository();

        try {
            System.out.println("Starting ledger audit...");
            auditRepository.verifySuccessfulTransactions();
        } catch (Exception e) {
            System.err.println("Audit failed: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
