package com.CSA.BankingBeyond;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

/*
 * This is the service class for the balance service.
 * It is used to calculate the balance of the user.
 * It is also used to get the most recent transaction's balance.
 * It is also used to get the all transactions.
 * It is also used to get the transaction map.
 * It is also used to get the transaction parser.
 * It is also used to get the transaction.
 */

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
