package com.example.bookstore.controller;

import com.example.bookstore.exceptions.DuplicateUsernameException;
import com.example.bookstore.models.User;
import com.example.bookstore.services.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/login")
    public String login() {
        return "intro/login";
    }

    @PostMapping("/register") // continue from here
    public String register(@ModelAttribute("user")User user, BindingResult result, Model model) {
        model.addAttribute("user", new User());
        try{
            userService.createUser(user.getUsername(), user.getPassword());
        } catch (DuplicateUsernameException ex) {
            result.rejectValue(
                    "username",
                    "error.user",
                    ex.getMessage()
            );
        }
        return "intro/login";
    }

    @GetMapping("/register")
    public String getRegister() {
        return "intro/register";
    }


}
