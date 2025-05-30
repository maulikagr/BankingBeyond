package com.CSA.BankingBeyond;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Component;

@Component
public class TransactionMap {
    private HashMap<String, List<Transaction>> map;

    public TransactionMap(List<Transaction> transactions) {
        map = new HashMap<>();
        updateMap(transactions);  // Use updateMap to initialize the map
    }

    public void addToMap(String key, Transaction tx) {
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
        Set<Transaction> results = new HashSet<>();
        
        // Search for partial matches
        for (String mapKey : map.keySet()) {
            if (mapKey.contains(lowerKey)) {
                results.addAll(map.get(mapKey));
            }
        }
        
        return new ArrayList<>(results);
    }

    public List<Transaction> getAllTransactions() {
        Set<Transaction> uniqueTransactions = new HashSet<>();
        for (List<Transaction> transactions : map.values()) {
            uniqueTransactions.addAll(transactions);
        }
        return new ArrayList<>(uniqueTransactions);
    }

    public void updateMap(List<Transaction> transactions) {
        // Clear existing map
        map.clear();
        
        // Rebuild map with all transactions
        for (Transaction tx : transactions) {
            if (tx == null) continue;
            
            addToMap(tx.getCategory(), tx);
            addToMap(tx.getDescription(), tx);
            addToMap(tx.getDate(), tx);
        }
    }
}
