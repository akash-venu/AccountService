// Exception class (InsufficientBalanceException.java)
package com.tekarch.accountServiceMicroservice.Exception;

import java.math.BigDecimal;

public class InsufficientBalanceException extends RuntimeException {
    private Long accountId;
    private BigDecimal amount;

    // Constructor that accepts a message, accountId, and amount
    public InsufficientBalanceException(String message) {
        super(message);
    }

    // Constructor that accepts accountId and amount
    public InsufficientBalanceException(Long accountId, BigDecimal amount) {
        super("Insufficient balance in accountId: " + accountId + ", Amount: " + amount);
        this.accountId = accountId;
        this.amount = amount;
    }

    public Long getAccountId() {
        return accountId;
    }

    public BigDecimal getAmount() {
        return amount;
    }
}