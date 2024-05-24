package com.example.demo.service;

import com.example.demo.model.BankAccount;
import com.example.demo.model.User;
import com.example.demo.model.UserRegistrationRequest;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public User registerNewUser(UserRegistrationRequest request) {
        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        BankAccount bankAccount = new BankAccount();
        bankAccount.setInitialAmount(request.getInitialAmount());
        bankAccount.setBalance(request.getInitialAmount());

        user.setBankAccount(bankAccount);
        user.setPhones(request.getPhones());
        user.setEmails(request.getEmails());
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setMiddleName(request.getMiddleName());
        user.setBirthDate(request.getBirthDate());

        return userRepository.save(user);
    }

    public User updatePhones(String username, Set<String> phones) {
        User user = userRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("Пользователь не найден"));
        user.setPhones(phones);
        return userRepository.save(user);
    }

    public User updateEmails(String username, Set<String> emails) {
        User user = userRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("Пользователь не найден"));
        user.setEmails(emails);
        return userRepository.save(user);
    }

    public User deletePhone(String username, String phone) {
        User user = userRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("Пользователь не найден"));
        Set<String> phones = user.getPhones();
        if (phones.size() > 1) {
            phones.remove(phone);
        }
        user.setPhones(phones);
        return userRepository.save(user);
    }

    public User deleteEmail(String username, String email) {
        User user = userRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("Пользователь не найден"));
        Set<String> emails = user.getEmails();
        if (emails.size() > 1) {
            emails.remove(email);
        }
        user.setEmails(emails);
        return userRepository.save(user);
    }

    public Page<User> searchUsers(String firstName, String lastName, String middleName, String birthDate, String phone, String email, Pageable pageable) {
        return userRepository.searchUsers(firstName, lastName, middleName, birthDate, phone, email, pageable);
    }
}
