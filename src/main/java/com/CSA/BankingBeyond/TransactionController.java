package com.CSA.BankingBeyond;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private TransactionParser transactionParser;

    @Autowired
    private SpendingLimitService spendingLimitService;

    @GetMapping("/make-transaction")
    public String transactionForm(Model model) {
        return "make-transaction";
    }

    @PostMapping("/make-transaction")
    public String makeTransactionSubmit(@RequestParam String description, 
                                      @RequestParam double amount,
                                      @RequestParam String category,
                                      Model model) {
        try {
            transactionService.addTransaction(description, amount, category);
            // Check spending limit after adding transaction
            spendingLimitService.checkDailySpending();
            return "redirect:/transaction";
        } catch (Exception e) {
            model.addAttribute("error", "Error creating transaction: " + e.getMessage());
            return "make-transaction";
        }
    }

    @GetMapping("/transaction")
    public String showTransactions(Model model) {
        List<Transaction> transactions = transactionParser.getAllTransactions();
        model.addAttribute("transactions", transactions);
        return "transaction";
    }

    @GetMapping("/search")
    @ResponseBody
    public List<Transaction> searchTransactions(@RequestParam(required = false) String keyword) {
        return transactionParser.searchTransactions(keyword);
    }

    @GetMapping("/")
    public String home(Model model) {
        List<Transaction> transactions = transactionParser.getAllTransactions();
        double currentBalance = transactions.isEmpty() ? 0.0 : 
            transactions.get(transactions.size() - 1).getBalance();
        model.addAttribute("currentBalance", currentBalance);
        return "index";
    }
}
