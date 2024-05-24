package com.example.demo;

import com.example.demo.model.BankAccount;
import com.example.demo.model.User;
import com.example.demo.repository.BankAccountRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.BankAccountService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Transactional
public class BankAccountServiceTests {

    @Autowired
    private BankAccountService bankAccountService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BankAccountRepository bankAccountRepository;

    private User user1;
    private User user2;

    @BeforeEach
    public void setUp() {
        user1 = userRepository.findByUsername("user1").orElseThrow();
        user2 = userRepository.findByUsername("user2").orElseThrow();
    }

    @Test
    public void testSuccessfulTransfer() {
        double amount = 100.0;
        double initialBalanceUser1 = user1.getBankAccount().getBalance();
        double initialBalanceUser2 = user2.getBankAccount().getBalance();

        bankAccountService.transferMoney(user1.getBankAccount().getId(), user2.getBankAccount().getId(), amount);

        BankAccount updatedAccount1 = bankAccountRepository.findById(user1.getBankAccount().getId()).orElseThrow();
        BankAccount updatedAccount2 = bankAccountRepository.findById(user2.getBankAccount().getId()).orElseThrow();

        assertThat(updatedAccount1.getBalance()).isEqualTo(initialBalanceUser1 - amount);
        assertThat(updatedAccount2.getBalance()).isEqualTo(initialBalanceUser2 + amount);
    }

    @Test
    public void testInsufficientFunds() {
        double amount = 2000.0;
        double initialBalanceUser1 = user1.getBankAccount().getBalance();

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                bankAccountService.transferMoney(user1.getBankAccount().getId(), user2.getBankAccount().getId(), amount));

        assertThat(exception.getMessage()).isEqualTo("Insufficient balance");

        BankAccount updatedAccount1 = bankAccountRepository.findById(user1.getBankAccount().getId()).orElseThrow();
        BankAccount updatedAccount2 = bankAccountRepository.findById(user2.getBankAccount().getId()).orElseThrow();

        assertThat(updatedAccount1.getBalance()).isEqualTo(initialBalanceUser1);
        assertThat(updatedAccount2.getBalance()).isEqualTo(user2.getBankAccount().getBalance());
    }
}
