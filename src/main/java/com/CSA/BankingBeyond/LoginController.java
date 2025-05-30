package com.CSA.BankingBeyond;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class LoginController {

    @Autowired
    private UserService userService;

    @GetMapping("/login")
    public String loginForm() {
        return "login";
    }

    @PostMapping("/login")
    public ModelAndView loginSubmit(@RequestParam String username, @RequestParam String password) {
        ModelAndView modelAndView = new ModelAndView("redirect:/login.html");
        User user = userService.findByUsername(username);

        if (user != null && user.getPassword().equals(password)) {
            modelAndView.setViewName("redirect:/index.html");
            modelAndView.addObject("username", username);
        } else {
            modelAndView.addObject("error", "Invalid username or password");
        }
        return modelAndView;
    }
}
