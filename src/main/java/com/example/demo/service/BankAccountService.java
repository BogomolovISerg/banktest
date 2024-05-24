package com.example.demo.service;

import com.example.demo.model.BankAccount;
import com.example.demo.repository.BankAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Service
public class BankAccountService {

    @Autowired
    private BankAccountRepository bankAccountRepository;

    private final Lock lock = new ReentrantLock();

    @Transactional
    public void transferMoney(Long fromAccountId, Long toAccountId, Double amount) {
        lock.lock();
        try {
            BankAccount fromAccount = bankAccountRepository.findById(fromAccountId)
                    .orElseThrow(() -> new IllegalArgumentException("Неверный идентификатор аккаунта"));
            BankAccount toAccount = bankAccountRepository.findById(toAccountId)
                    .orElseThrow(() -> new IllegalArgumentException("Неверный идентификатор аккаунта"));

            if (fromAccount.getBalance() < amount) {
                throw new IllegalArgumentException("Недостаточно средств на балансе");
            }

            fromAccount.setBalance(fromAccount.getBalance() - amount);
            toAccount.setBalance(toAccount.getBalance() + amount);

            bankAccountRepository.save(fromAccount);
            bankAccountRepository.save(toAccount);
        } finally {
            lock.unlock();
        }
    }

    public void increaseBalances() {
        List<BankAccount> accounts = bankAccountRepository.findAll();
        for (BankAccount account : accounts) {
            Double newBalance = account.getBalance() * 1.05;
            if (newBalance <= account.getInitialAmount() * 2.07) {
                account.setBalance(newBalance);
            }
            bankAccountRepository.save(account);
        }
    }
}
