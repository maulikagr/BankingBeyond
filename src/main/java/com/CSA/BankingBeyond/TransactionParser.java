package com.CSA.BankingBeyond;

import org.springframework.stereotype.Service;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;

@Service
public class TransactionParser {
    private List<Transaction> allTransactions;
    private TransactionMap transactionMap;

    public TransactionParser() {
        this.allTransactions = new ArrayList<>();
        this.transactionMap = new TransactionMap(allTransactions);
        List<Transaction> parsedTransactions = parseTransactions();
        this.allTransactions.addAll(parsedTransactions);
        this.transactionMap.updateMap(allTransactions);
    }

    public List<Transaction> getAllTransactions() {
        return allTransactions;
    }

    public void addTransaction(Transaction transaction) {
        if (transaction != null) {
            allTransactions.add(transaction);
            transactionMap.updateMap(allTransactions);
        }
    }

    public List<Transaction> searchTransactions(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return allTransactions;
        }
        return transactionMap.search(keyword.trim());
    }

    public TransactionMap getTransactionMap() {
        return transactionMap;
    }

    public List<Transaction> parseTransactions() {
        List<Transaction> transactions = new ArrayList<>();
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("transactions.txt");

        if (inputStream == null) {
            System.out.println("File not found!");
            return transactions;
        }

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.trim().split("\\s{2,}");
                if (parts.length >= 5) {
                    try {
                        String date = parts[0];
                        String category = parts[1];
                        String description = parts[2];
                        double amount = Double.parseDouble(parts[3].replaceAll("[^\\d.-]", ""));
                        double balance = Double.parseDouble(parts[4].replaceAll("[^\\d.-]", ""));
                        transactions.add(new Transaction(date, category, description, amount, balance));
                    } catch (NumberFormatException e) {
                        System.out.println("Skipping invalid transaction: " + line);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return transactions;
    }

}
