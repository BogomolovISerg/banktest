package com.example.demo;

import com.example.demo.model.BankAccount;
import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.HashSet;

@Component
public class DataLoader implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Override
    public void run(String... args) throws Exception {
        if (userRepository.count() == 0) {
            createUser("john_doe", "password", "John", "Doe", "Middle", LocalDate.of(1990, 1, 1), 1000.0);
            createUser("jane_doe", "password", "Jane", "Doe", "Middle", LocalDate.of(1992, 2, 2), 2000.0);
        }
    }

    private void createUser(String username, String password, String firstName, String lastName, String middleName, LocalDate birthDate, double initialAmount) {
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setMiddleName(middleName);
        user.setBirthDate(birthDate);

        user.setPhones(new HashSet<>());
        user.setEmails(new HashSet<>());

        BankAccount bankAccount = new BankAccount();
        bankAccount.setBalance(initialAmount);
        bankAccount.setInitialAmount(initialAmount);

        user.setBankAccount(bankAccount);

        userRepository.save(user);
    }
}
