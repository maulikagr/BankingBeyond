package com.CSA.BankingBeyond;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

/*
 * This is the service class for the spending limit service.
 * It is used to check the daily spending limit.
 * It is also used to send an email to the user if the spending limit is exceeded.
 */

@Service
public class SpendingLimitService {

    @Autowired
    private TransactionParser transactionParser;

    @Autowired
    private UserService userService;

    @Autowired
    private JavaMailSender mailSender;

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("MM/dd/yyyy");

    private Double dailyLimit;
    private String userEmail;

    public void setLimit(Double limit) {
        this.dailyLimit = limit;
        System.out.println("Daily limit set to: $" + limit);
    }

    public Double getCurrentLimit() {
        return dailyLimit;
    }

    public void setUserEmail(String email) {
        this.userEmail = email;
        System.out.println("User email set to: " + email);
    }

    public void checkDailySpending() {
        System.out.println("Checking daily spending...");
        System.out.println("Daily limit: " + (dailyLimit != null ? "$" + dailyLimit : "not set"));
        System.out.println("User email: " + (userEmail != null ? userEmail : "not set"));

        if (dailyLimit == null || userEmail == null) {
            System.out.println("Cannot check spending: limit or email not set");
            return;
        }

        LocalDate today = LocalDate.now();
        List<Transaction> todayTransactions = transactionParser.getAllTransactions().stream()
            .filter(tx -> {
                try {
                    LocalDate txDate = LocalDate.parse(tx.getDate(), DATE_FORMATTER);
                    return txDate.equals(today) && tx.getAmount() < 0; // Only consider spending (negative amounts)
                } catch (Exception e) {
                    System.err.println("Error parsing date: " + tx.getDate());
                    return false;
                }
            })
            .toList();

        System.out.println("Found " + todayTransactions.size() + " transactions today");

        double totalSpent = todayTransactions.stream()
            .mapToDouble(tx -> Math.abs(tx.getAmount()))
            .sum();

        System.out.println("Total spent today: $" + totalSpent);

        if (totalSpent > dailyLimit) {
            System.out.println("Limit exceeded! Sending email...");
            System.out.println("Total spent: $" + totalSpent + " exceeds limit of $" + dailyLimit);
            sendOverspendingEmail(totalSpent);
        } else {
            System.out.println("Within spending limit");
        }
    }

    private void sendOverspendingEmail(double totalSpent) {
        System.out.println("Preparing to send email to: " + userEmail);
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(userEmail);
        message.setSubject("Daily Spending Limit Exceeded");
        message.setText(String.format(
            "Dear User,\n\n" +
            "You have exceeded your daily spending limit of $%.2f.\n" +
            "Total spent today: $%.2f\n\n" +
            "Please review your recent transactions and adjust your spending accordingly.\n\n" +
            "Best regards,\n" +
            "BankingBeyond Team",
            dailyLimit, totalSpent
        ));

        try {
            System.out.println("Attempting to send email...");
            mailSender.send(message);
            System.out.println("Email sent successfully to " + userEmail);
        } catch (Exception e) {
            System.err.println("Failed to send email: " + e.getMessage());
            System.err.println("Error details:");
            e.printStackTrace();
        }
    }
} 