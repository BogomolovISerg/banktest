package com.example.demo.controller;

import com.example.demo.model.User;
import com.example.demo.service.BankAccountService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/accounts")
public class BankAccountController {

    @Autowired
    private BankAccountService bankAccountService;

    @PostMapping("/transfer")
    public ResponseEntity<?> transferMoney(Authentication authentication, @RequestParam Long toAccountId, @RequestParam Double amount) {
        User user = (User) authentication.getPrincipal();
        Long fromAccountId = user.getBankAccount().getId();

        bankAccountService.transferMoney(fromAccountId, toAccountId, amount);

        log.info("Запрос на перевод средств от пользователя: {}", authentication.getName());

        return ResponseEntity.ok("Перевод средств выполнен.");
    }
}
