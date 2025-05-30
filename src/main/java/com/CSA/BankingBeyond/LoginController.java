package com.CSA.BankingBeyond;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/*
 * This is the controller class for the login service.
 * It is used to login the user.
 * It is also used to create an account.
 * It is also used to get the login page.
 * It is also used to get the create account page.
 */

@Controller
public class LoginController {

    @Autowired
    private UserService userService;

    @Autowired
    private SpendingLimitService spendingLimitService;

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    @PostMapping("/login")
    public String login(@RequestParam String username, @RequestParam String password, Model model) {
        System.out.println("Attempting login for user: " + username);
        User user = userService.findByUsername(username);
        if (user != null && user.getPassword().equals(password)) {
            System.out.println("Login successful, setting email for spending limit notifications");
            spendingLimitService.setUserEmail(username);
            return "redirect:/";
        }
        System.out.println("Login failed for user: " + username);
        model.addAttribute("error", "Invalid username or password");
        return "login";
    }

    @GetMapping("/create-account")
    public String createAccountPage() {
        return "create-account";
    }

    @PostMapping("/create-account")
    public String createAccount(@RequestParam String username, @RequestParam String password, Model model) {
        System.out.println("Attempting to create account for: " + username);
        User existingUser = userService.findByUsername(username);
        if (existingUser != null) {
            System.out.println("Account creation failed - username already exists: " + username);
            model.addAttribute("error", "Username already exists");
            return "create-account";
        }

        User newUser = new User();
        newUser.setUsername(username);
        newUser.setPassword(password);
        userService.save(newUser);

        System.out.println("Account created successfully, setting email for spending limit notifications");
        spendingLimitService.setUserEmail(username);
        return "redirect:/";
    }
}
