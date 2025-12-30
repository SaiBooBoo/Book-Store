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

    @PostMapping("/register")
    public String register(
            @ModelAttribute("user") User user,
            BindingResult result) {

        if (result.hasErrors()) {
            return "intro/register";
        }

        try {
            userService.createUser(user.getUsername(), user.getPassword());
        } catch (DuplicateUsernameException ex) {
            result.rejectValue("username", null, ex.getMessage());
            return "intro/register";
        }

        return "redirect:/login";
    }

    @GetMapping("/register")
    public String getRegister(Model model) {
        model.addAttribute("user", new User());
        return "intro/register";
    }


}
