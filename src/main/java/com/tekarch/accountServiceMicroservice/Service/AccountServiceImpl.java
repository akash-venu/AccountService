package com.tekarch.accountServiceMicroservice.Service;

import com.tekarch.accountServiceMicroservice.DTO.UserDTO;
import com.tekarch.accountServiceMicroservice.Models.Account;
import com.tekarch.accountServiceMicroservice.Repositories.AccountRepository;
import com.tekarch.accountServiceMicroservice.Service.Interfaces.AccountService;
import com.tekarch.accountServiceMicroservice.Exception.AccountNotFoundException;
import com.tekarch.accountServiceMicroservice.Exception.InsufficientBalanceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class AccountServiceImpl implements AccountService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private RestTemplate restTemplate;

    private static final String USER_SERVICE_URL = "http://localhost:8080/api/users";

    @Override
    public Account createAccount(Account account) {
        if (accountRepository.existsByAccountNumber(account.getAccountNumber())) {
            throw new RuntimeException("Account number already exists");
        }
        return accountRepository.save(account);
    }

    @Override
    public List<Account> getAllAccounts() {
        return accountRepository.findAll();
    }

    @Override
    public Account getAccountById(Long accountId) {
        Optional<Account> account = accountRepository.findByAccountId(accountId);
        if (account.isEmpty()) {
            throw new AccountNotFoundException("Account not found for accountId: " + accountId);
        }
        return account.get();
    }

    @Override
    public Account updateAccount(Account account) {
        if (!accountRepository.existsByAccountId(account.getAccountId())) {
            throw new AccountNotFoundException("Account not found for accountId: " + account.getAccountId());
        }
        return accountRepository.save(account);
    }

    @Override
    public boolean deleteAccount(Long accountId) {
        Optional<Account> account = accountRepository.findByAccountId(accountId);
        if (account.isEmpty()) {
            throw new AccountNotFoundException("Account not found for accountId: " + accountId);
        }
        accountRepository.deleteById(accountId);
        return true;
    }

    @Override
    public Account getAccountByUserId(Long userId) {
        String userUrl = "http://localhost:8080/api/users/" + userId;
        UserDTO userDTO = restTemplate.getForObject(userUrl, UserDTO.class);

        if (userDTO == null) {
            throw new AccountNotFoundException("User not found for userId: " + userId);
        }

        Optional<Account> account = accountRepository.findByUserId(userId);
        if (account.isEmpty()) {
            throw new AccountNotFoundException("Account not found for userId: " + userId);
        }
        return account.get();
    }

    @Override
    public Optional<BigDecimal> getAccountBalance(Long accountId) {
        Optional<BigDecimal> balance = accountRepository.findBalanceByAccountId(accountId);
        if (balance.isEmpty()) {
            throw new AccountNotFoundException("Account not found for accountId: " + accountId);
        }
        return balance;
    }

    @Override
    public Account transferFunds(Long fromAccountId, Long toAccountId, BigDecimal amount) {
        Optional<Account> fromAccountOpt = accountRepository.findByAccountId(fromAccountId);
        Optional<Account> toAccountOpt = accountRepository.findByAccountId(toAccountId);

        if (fromAccountOpt.isEmpty()) {
            throw new AccountNotFoundException("Source account not found for accountId: " + fromAccountId);
        }
        if (toAccountOpt.isEmpty()) {
            throw new AccountNotFoundException("Destination account not found for accountId: " + toAccountId);
        }

        Account fromAccount = fromAccountOpt.get();
        Account toAccount = toAccountOpt.get();

        // Check for sufficient balance
        if (fromAccount.getBalance().compareTo(amount) < 0) {
            throw new InsufficientBalanceException(fromAccountId, amount);  // Correct constructor
        }

        // Perform fund transfer (debit from source and credit to destination)
        fromAccount.setBalance(fromAccount.getBalance().subtract(amount));
        toAccount.setBalance(toAccount.getBalance().add(amount));

        // Save updated accounts
        accountRepository.save(fromAccount);
        accountRepository.save(toAccount);

        return fromAccount; // Or return the updated toAccount if needed
    }

    @Override
    public boolean accountExistsByAccountNumber(String accountNumber) {
        return accountRepository.existsByAccountNumber(accountNumber);
    }
}