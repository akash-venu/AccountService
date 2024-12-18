package com.tekarch.accountServiceMicroservice.Service.Interfaces;

import com.tekarch.accountServiceMicroservice.Models.Account;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface AccountService {
    Account createAccount(Account account);
    List<Account> getAllAccounts();
    Account getAccountById(Long accountId);
    Account updateAccount(Account account);
    boolean deleteAccount(Long accountId);
    Account getAccountByUserId(Long userId);  // This should match the implementation
    Optional<BigDecimal> getAccountBalance(Long accountId);
    Account transferFunds(Long fromAccountId, Long toAccountId, BigDecimal amount);
    boolean accountExistsByAccountNumber(String accountNumber);
}