package com.CSA.BankingBeyond;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TransactionParser {
    public static void main(String[] args) {
        TransactionParser parser = new TransactionParser();
        List<Transaction> transactions = parser.parseTransactions();
        System.out.println(transactions);
    }
    @GetMapping("/bank")
    public List<Transaction> parseTransactions() {
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("transactions.txt");

        if (inputStream == null) {
            System.out.println("File not found in resources folder.");
            return new ArrayList<>();
        }

        List<Transaction> transactions = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            reader.readLine();

            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\s{2,}");
                if (parts.length == 4) {
                    String date = parts[0];
                    String description = parts[1];
                    double amount = parseAmount(parts[2]);
                    double balance = parseBalance(parts[3]);

                    transactions.add(new Transaction(date, description, amount, balance));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return transactions;
    }

    private double parseAmount(String amountStr) {
        amountStr = amountStr.replaceAll("[^\\d.-]", "");
        return Double.parseDouble(amountStr);
    }

    private double parseBalance(String balanceStr) {
        balanceStr = balanceStr.replaceAll("[^\\d.-]", "");
        return Double.parseDouble(balanceStr);
    }
}
