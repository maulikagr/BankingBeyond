package com.CSA.BankingBeyond;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TransactionService {

    private List<Transaction> transactions = new ArrayList<>();
    private double currentBalance = 0.0; // Track current balance

    public List<Transaction> getAllTransactions() {
        return transactions;
    }

    public void addTransaction(Transaction transaction) {
        // Calculate balance for the new transaction based on current balance
        transaction = new Transaction(transaction.getDate(), transaction.getCategory(),
                transaction.getDescription(), transaction.getAmount(), currentBalance);

        transactions.add(transaction);
        currentBalance = transaction.getBalance(); // Update current balance after adding transaction

        // Optionally, you can update your transaction map or storage mechanism here
    }

    // Add more methods as needed, such as searchTransactions, deleteTransaction, etc.
}
