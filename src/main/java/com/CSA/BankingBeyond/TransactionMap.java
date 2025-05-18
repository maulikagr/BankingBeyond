package com.CSA.BankingBeyond;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TransactionMap {
    private HashMap<String, List<Transaction>> map;

    public TransactionMap(List<Transaction> transactions) {
        map = new HashMap<>();

        for (Transaction tx : transactions) {
            if (tx == null) continue;

            addToMap(tx.getCategory(), tx);
            addToMap(tx.getDescription(), tx);
            addToMap(tx.getDate(), tx);
        }
    }

    private void addToMap(String key, Transaction tx) {
        if (key == null || tx == null) return;

        String lowerKey = key.toLowerCase();
        if (!map.containsKey(lowerKey)) {
            map.put(lowerKey, new ArrayList<Transaction>());
        }
        map.get(lowerKey).add(tx);
    }

    public List<Transaction> search(String key) {
        if (key == null) return new ArrayList<Transaction>();

        String lowerKey = key.toLowerCase();
        if (map.containsKey(lowerKey)) {
            return map.get(lowerKey);
        } else {
            return new ArrayList<Transaction>();
        }
    }

    // For testing only
    public static void main(String[] args) {
        TransactionParser parser = new TransactionParser();
        List<Transaction> transactions = parser.getAllTransactions(); // Updated to use public getter

        TransactionMap txMap = new TransactionMap(transactions);

        System.out.println("Transactions for category 'Food':");
        List<Transaction> foodTx = txMap.search("Food");
        for (Transaction tx : foodTx) {
            System.out.println(tx);
        }

        System.out.println("\nTransactions for description 'Taco Bell':");
        List<Transaction> tacoTx = txMap.search("Taco Bell");
        for (Transaction tx : tacoTx) {
            System.out.println(tx);
        }

        System.out.println("\nTransactions on '01/10/2025':");
        List<Transaction> dateTx = txMap.search("01/10/2025");
        for (Transaction tx : dateTx) {
            System.out.println(tx);
        }
    }
}
