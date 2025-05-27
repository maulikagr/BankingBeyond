package com.CSA.BankingBeyond;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.stereotype.Component;

@Component
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
        if (map.containsKey(lowerKey)) {
            return map.get(lowerKey);
        } else {
            return new ArrayList<Transaction>();
        }
    }

}
