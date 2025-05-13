package com.CSA.BankingBeyond;

public class Transaction {
    private String date;
    private String description;
    private double amount;
    private double balance;

    public Transaction(String date, String description, double amount, double balance) {
        this.date = date;
        this.description = description;
        this.amount = amount;
        this.balance = balance;
    }

    public String getDate() {
        return date;
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
                ", description='" + description + '\'' +
                ", amount=" + amount +
                ", balance=" + balance +
                '}';
    }
}


