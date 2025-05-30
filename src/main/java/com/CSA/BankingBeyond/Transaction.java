package com.CSA.BankingBeyond; 

public class Transaction {
    private String date;
    private String description;
    private String category;
    private double amount;
    private double balance;

    public Transaction() {
    }

    public Transaction(String date, String description, String category, double amount, double balance) {
        this.date = date;
        this.description = description;
        this.category = category;
        this.amount = amount;
        this.balance = balance;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
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



