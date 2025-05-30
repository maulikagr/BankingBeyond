package com.CSA.BankingBeyond;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
public class TransactionController {

    @Autowired
    private TransactionService transactionService; // Inject your transaction service

    @GetMapping("/make-transaction")
    public String transactionForm(Model model) {
        model.addAttribute("transaction", new Transaction("", "", "", 0.0, 0.0)); // Initialize a new transaction object
        return "redirect:/make-transaction.html"; // Return make-transaction.html (create this file in resources/templates)
    }

    @PostMapping("/make-transaction")
    public String makeTransactionSubmit(Transaction transaction, Model model) {
        // Process the transaction (save to database, update service, etc.)
        transactionService.addTransaction(transaction);
        
        model.addAttribute("message", "Transaction successful!");
        model.addAttribute("transaction", new Transaction("", "", "", 0.0, 0.0)); // Clear the form fields
        
        return "redirect:/transaction"; // Redirect to the transaction page endpoint
    }

    @GetMapping("/transaction")
    public String showTransactions(Model model) {
        List<Transaction> transactions = transactionService.getAllTransactions();
        model.addAttribute("transactions", transactions);
        return "transaction";
    }

    @GetMapping("/search")
    @ResponseBody
    public List<Transaction> searchTransactions(@RequestParam(required = false) String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return transactionService.getAllTransactions();
        }
        return transactionService.searchTransactions(keyword.trim());
    }
}
