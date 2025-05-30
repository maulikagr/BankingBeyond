package com.CSA.BankingBeyond;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

/*
 * This is the service class for the transaction service.
 * It is used to add a transaction.
 * It is also used to get the all transactions.
 * It is also used to search for a transaction.
 */

@Service
public class TransactionService {
    private final TransactionParser transactionParser;
    private final BalanceService balanceService;
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("MM/dd/yyyy");

    @Autowired
    public TransactionService(TransactionParser transactionParser, BalanceService balanceService) {
        this.transactionParser = transactionParser;
        this.balanceService = balanceService;
    }

    public void addTransaction(String description, double amount, String category) {
        Transaction transaction = new Transaction();
        transaction.setDescription(description);
        transaction.setAmount(amount);
        transaction.setDate(LocalDate.now().format(DATE_FORMATTER));
        transaction.setCategory(category);
        
        // Calculate new balance
        double newBalance = balanceService.calculateNewBalance(transaction);
        transaction.setBalance(newBalance);
        
        transactionParser.addTransaction(transaction);
    }

    public List<Transaction> getAllTransactions() {
        return transactionParser.getAllTransactions();
    }

    public List<Transaction> searchTransactions(String query) {
        return transactionParser.searchTransactions(query);
    }
}
