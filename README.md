# Transactional Ledger & Audit Processing System

A Java-based banking transaction system using Oracle Database to process account transfers, maintain ledger entries, and verify transaction integrity.

## Features

- Account creation and balance retrieval
- Money transfers between bank accounts
- Database transaction management with commit and rollback
- Account locking to help prevent concurrent transfer conflicts
- Automatic debit and credit ledger entries
- Insufficient-balance validation
- Missing-account validation
- Audit verification of successful transactions against ledger entries

## Technology Stack

- **Language:** Java 17
- **Build Tool:** Apache Maven
- **Database:** Oracle Database Free
- **JDBC Driver:** Oracle JDBC
- **Containerization:** Docker
- **Version Control:** Git and GitHub

## Project Structure

```text
transactional-ledger-audit-system/
├── .mvn/
├── src/
│   ├── main/
│   │   ├── java/com/banking/
│   │   │   ├── model/
│   │   │   ├── repository/
│   │   │   └── service/
│   │   └── resources/db/schema.sql
│   └── test/java/com/banking/
├── .gitignore
├── pom.xml
└── README.md
```

## Database Configuration

The application connects to Oracle Database using JDBC.

Configure the database connection in `DatabaseConnection.java` and provide the database password through the `DB_PASSWORD` environment variable.

Do not commit passwords, tokens, or other secrets to GitHub.

## Build and Test

Ensure Java 17, Maven, Docker, and an Oracle Database instance are available.

Run the automated tests:

```bash
mvn clean test
```

The database-backed tests also require the Oracle database to be running and correctly configured.

## Core Transaction Workflow

1. Validate the source account, destination account, and transfer amount.
2. Lock the relevant account records.
3. Check the source account balance.
4. Update the source and destination balances.
5. Record the successful transaction.
6. Insert matching debit and credit ledger entries.
7. Commit the database transaction, or roll back if an error occurs.

## Audit Verification

The audit component checks successful transactions for matching debit and credit ledger entries with the correct accounts and amounts.

## Learning Objectives

- JDBC database connectivity
- SQL transaction management
- ACID transaction principles
- Repository and service layer design
- Financial ledger consistency
- Exception handling and automated testing
- Git-based project version control

## Disclaimer

This is an educational project and is not intended for production banking use.
