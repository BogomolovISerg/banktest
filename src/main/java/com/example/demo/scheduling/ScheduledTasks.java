package com.example.demo.scheduling;

import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
public class ScheduledTasks {

    @Autowired
    private UserRepository userRepository;

    @Scheduled(fixedRate = 60000) // Раз в минуту
    @Transactional
    public void increaseBalances() {
        List<User> users = userRepository.findAll();
        for (User user : users) {
            double initialAmount = user.getBankAccount().getInitialAmount();
            double currentBalance = user.getBankAccount().getBalance();
            double newBalance = currentBalance * 1.05;

            // Проверяем, что новый баланс не превышает 207% от начального депозита
            if (newBalance > initialAmount * 2.07) {
                newBalance = initialAmount * 2.07;
            }

            user.getBankAccount().setBalance(newBalance);
            userRepository.save(user);
        }
    }
}
