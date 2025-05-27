package com.CSA.BankingBeyond;

import java.util.List;

import org.springframework.stereotype.Service;

@Service
public class TransactionService {
    private final TransactionMap transactionMap;
    private double currentBalance = 0.0;

    public TransactionService(TransactionMap transactionMap) {
        this.transactionMap = transactionMap;
    }

    public void addTransaction(Transaction transaction) {
        if (transaction == null) return;

        double newBalance = currentBalance + transaction.getAmount();
        Transaction updatedTransaction = new Transaction(
                transaction.getDate(),
                transaction.getCategory(),
                transaction.getDescription(),
                transaction.getAmount(),
                newBalance);
        currentBalance = newBalance;

        transactionMap.addToMap(updatedTransaction.getCategory(), updatedTransaction);
        transactionMap.addToMap(updatedTransaction.getDescription(), updatedTransaction);
        transactionMap.addToMap(updatedTransaction.getDate(), updatedTransaction);
    }
    
}
