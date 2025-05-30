package com.CSA.BankingBeyond;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/*
 * This is the controller class for the spending limit service.
 * It is used to set the spending limit for the user.
 * It is also used to get the spending limit form.
 * It is also used to set the spending limit.
 */

@Controller
public class SpendingLimitController {

    @Autowired
    private SpendingLimitService spendingLimitService;

    @GetMapping("/set-limit")
    public String setLimitForm(Model model) {
        Double currentLimit = spendingLimitService.getCurrentLimit();
        model.addAttribute("currentLimit", currentLimit);
        return "set-limit";
    }

    @PostMapping("/set-limit")
    public String setLimit(@RequestParam Double limit, Model model) {
        spendingLimitService.setLimit(limit);
        model.addAttribute("message", "Daily spending limit has been set to $" + limit);
        return "set-limit";
    }
} 