package com.CSA.BankingBeyond;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;

@RestController
public class TransactionParser {
    private List<Transaction> allTransactions;
    public TransactionMap transactionMap;

    public TransactionParser() {
        this.allTransactions = parseTransactions();
        this.transactionMap = new TransactionMap(allTransactions);
    }

    @GetMapping("/bank")
    public List<Transaction> getAllTransactions() {
        return allTransactions;
    }

    @GetMapping("/search")
    public List<Transaction> searchTransactions(@RequestParam(required = false) String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return allTransactions;
        }
        return transactionMap.search(keyword.trim());
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
