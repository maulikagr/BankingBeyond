package com.CSA.BankingBeyond;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TransactionService {
    private final TransactionMap transactionMap;
    private final BalanceService balanceService;

    @Autowired
    public TransactionService(TransactionMap transactionMap, BalanceService balanceService) {
        this.transactionMap = transactionMap;
        this.balanceService = balanceService;
    }

    public void addTransaction(Transaction transaction) {
        if (transaction == null) return;

        double newBalance = balanceService.calculateNewBalance(transaction);
        transaction.setBalance(newBalance);

        transactionMap.addToMap(transaction.getCategory(), transaction);
        transactionMap.addToMap(transaction.getDescription(), transaction);
        transactionMap.addToMap(transaction.getDate(), transaction);
    }
}
