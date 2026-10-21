
-- Transactional Ledger & Audit Processing System
-- Oracle Database schema

CREATE TABLE accounts (
    account_id NUMBER(10) PRIMARY KEY,
    account_holder VARCHAR2(100) NOT NULL,
    balance NUMBER(19, 2) DEFAULT 0 NOT NULL,
    CONSTRAINT chk_account_balance
        CHECK (balance >= 0)
);

CREATE TABLE transactions (
    transaction_id NUMBER(19)
        GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    from_account_id NUMBER(10) NOT NULL,
    to_account_id NUMBER(10) NOT NULL,
    amount NUMBER(19, 2) NOT NULL,
    status VARCHAR2(20) DEFAULT 'SUCCESS' NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,

    CONSTRAINT fk_transaction_from
        FOREIGN KEY (from_account_id)
        REFERENCES accounts(account_id),

    CONSTRAINT fk_transaction_to
        FOREIGN KEY (to_account_id)
        REFERENCES accounts(account_id),

    CONSTRAINT chk_transaction_amount
        CHECK (amount > 0),

    CONSTRAINT chk_different_accounts
        CHECK (from_account_id <> to_account_id),

    CONSTRAINT chk_transaction_status
        CHECK (status IN ('SUCCESS', 'FAILED'))
);

CREATE TABLE ledger_entries (
    entry_id NUMBER(19)
        GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    transaction_id NUMBER(19) NOT NULL,
    account_id NUMBER(10) NOT NULL,
    entry_type VARCHAR2(10) NOT NULL,
    amount NUMBER(19, 2) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,

    CONSTRAINT fk_ledger_transaction
        FOREIGN KEY (transaction_id)
        REFERENCES transactions(transaction_id),

    CONSTRAINT fk_ledger_account
        FOREIGN KEY (account_id)
        REFERENCES accounts(account_id),

    CONSTRAINT chk_ledger_type
        CHECK (entry_type IN ('DEBIT', 'CREDIT')),

    CONSTRAINT chk_ledger_amount
        CHECK (amount > 0)
);
