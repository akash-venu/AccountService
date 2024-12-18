package com.tekarch.accountServiceMicroservice.Repositories;

import com.tekarch.accountServiceMicroservice.Models.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import java.math.BigDecimal;
import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Long> {

    // Find Account by Account ID
    Optional<Account> findByAccountId(Long accountId);

    // Check if Account exists by Account ID
    boolean existsByAccountId(Long accountId);

    // Find Account by User ID
    Optional<Account> findByUserId(Long userId);

    // Find Balance by Account ID
    Optional<BigDecimal> findBalanceByAccountId(Long accountId);

    // Check if Account exists by Account Number
    boolean existsByAccountNumber(String accountNumber);

    // Other necessary queries (if needed)
}