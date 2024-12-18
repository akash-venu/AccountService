package com.tekarch.accountServiceMicroservice.Controller;

import com.tekarch.accountServiceMicroservice.Models.Account;
import com.tekarch.accountServiceMicroservice.Service.Interfaces.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/accounts")
public class AccountController {

    @Autowired
    private AccountService accountService;


    // Create a new account
    @PostMapping
    public ResponseEntity<Account> createAccount(@RequestBody Account account) {
        Account createdAccount = accountService.createAccount(account);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdAccount);
    }

    // Get all accounts
    @GetMapping
    public ResponseEntity<List<Account>> getAllAccounts() {
        List<Account> accounts = accountService.getAllAccounts();
        return accounts.isEmpty() ?
                ResponseEntity.noContent().build() : ResponseEntity.ok(accounts);
    }

    // Get account by accountId
    @GetMapping("/{accountId}")
    public ResponseEntity<Account> getAccountById(@PathVariable Long accountId) {
        Account account = accountService.getAccountById(accountId);
        return account != null ? ResponseEntity.ok(account) :
                ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new Account());  // Return an empty account if not found
    }


    // Update an existing account
    @PutMapping("/{accountId}")
    public ResponseEntity<Account> updateAccount(@PathVariable Long accountId, @RequestBody Account account) {
        account.setAccountId(accountId);  // Set the accountId from the path variable
        Account updatedAccount = accountService.updateAccount(account);
        return updatedAccount != null ? ResponseEntity.ok(updatedAccount) :
                ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new Account());  // Return empty account if not found
    }

    // Delete an account
    @DeleteMapping("/{accountId}")
    public ResponseEntity<String> deleteAccount(@PathVariable Long accountId) {
        boolean isDeleted = accountService.deleteAccount(accountId);
        return isDeleted ? ResponseEntity.ok("Account deleted successfully.") :
                ResponseEntity.status(HttpStatus.NOT_FOUND).body("Account not found.");
    }
}