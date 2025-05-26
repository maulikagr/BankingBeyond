package com.CSA.BankingBeyond;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class LoginController {

    @Autowired
    private UserService userService;

    @GetMapping("/login")
    public String loginForm() {
        return "login"; // Return login.html (assuming you have a login.html in resources/templates)
    }

    @PostMapping("/login")
    public String loginSubmit(@RequestParam String username, @RequestParam String password, Model model) {
        User user = userService.findByUsername(username);
        if (user != null && user.getPassword().equals(password)) {
            // Authentication success
            model.addAttribute("username", username);
            return "redirect:/index.html"; // Redirect to home page after successful login
        } else {
            // Authentication failed
            model.addAttribute("error", "Invalid username or password");
            return "login"; // Return to login page with error message
        }
    }
}
