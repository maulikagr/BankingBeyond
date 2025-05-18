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
    private TransactionMap transactionMap;

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
            return allTransactions;  // Return all if search is empty
        }
        return transactionMap.search(keyword.trim());
    }

    // This method reads transactions.txt file and returns list of Transactions
    public List<Transaction> parseTransactions() {
        List<Transaction> transactions = new ArrayList<>();
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("transactions.txt");

        if (inputStream == null) {
            System.out.println("File not found!");
            return transactions;
        }

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            String line = reader.readLine(); // Skip header
            while ((line = reader.readLine()) != null) {
                String[] parts = line.trim().split("\\s{2,}");
                if (parts.length >= 5) {
                    String date = parts[0];
                    String category = parts[1];
                    String description = parts[2];
                    double amount = Double.parseDouble(parts[3].replaceAll("[^\\d.-]", ""));
                    double balance = Double.parseDouble(parts[4].replaceAll("[^\\d.-]", ""));
                    transactions.add(new Transaction(date, category, description, amount, balance));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return transactions;
    }

    // Inner class TransactionMap to keep hashmap of keywords -> transactions (student style)
    public class TransactionMap {
        private HashMap<String, List<Transaction>> map;

        public TransactionMap(List<Transaction> transactions) {
            map = new HashMap<>();
            for (Transaction tx : transactions) {
                addToMap(tx.getCategory(), tx);
                addToMap(tx.getDescription(), tx);
                addToMap(tx.getDate(), tx);
            }
        }

        private void addToMap(String key, Transaction tx) {
            key = key.toLowerCase();
            if (!map.containsKey(key)) {
                map.put(key, new ArrayList<Transaction>());
            }
            map.get(key).add(tx);
        }

        public List<Transaction> search(String key) {
            key = key.toLowerCase();
            if (map.containsKey(key)) {
                return map.get(key);
            }
            return new ArrayList<>();
        }
    }
}
