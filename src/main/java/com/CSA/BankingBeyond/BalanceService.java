package com.CSA.BankingBeyond;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class BalanceService {
    private final TransactionParser transactionParser;

    @Autowired
    public BalanceService(TransactionParser transactionParser) {
        this.transactionParser = transactionParser;
    }

    public double calculateNewBalance(Transaction transaction) {
        List<Transaction> allTransactions = transactionParser.getAllTransactions();
        if (allTransactions.isEmpty()) {
            return transaction.getAmount();
        }
        
        // Get the most recent transaction's balance
        Transaction lastTransaction = allTransactions.get(allTransactions.size() - 1);
        return lastTransaction.getBalance() + transaction.getAmount();
    }
}
