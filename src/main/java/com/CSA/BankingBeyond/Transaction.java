package com.CSA.BankingBeyond; 

public class Transaction {
    private String date;
    private String description;
    private double amount;
    private double balance;
    private String category;

    public Transaction(String date, String category, String description, double amount, double previousBalance) {
        this.date = date;
        this.description = description;
        this.amount = amount;
        this.category = category;

  
        this.balance = previousBalance + amount;
    }

    public String getDate() {
        return date;
    }
    public String getCategory() {
        return category;
    }
    public String getDescription() {
        return description;
    }

    public double getAmount() {
        return amount;
    }

    public double getBalance() {
        return balance;
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "date='" + date + '\'' +
                ", category='" + category + '\'' +
                ", description='" + description + '\'' +
                ", amount=" + amount +
                ", balance=" + balance +
                '}';
    }
}


