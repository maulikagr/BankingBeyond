package com.CSA.BankingBeyond;

import org.springframework.stereotype.Service;

@Service
public class BalanceService {
    private double currentBalance = 0.0;

    public double calculateNewBalance(Transaction transaction) {
        currentBalance += transaction.getAmount();
        return currentBalance;
    }
}
