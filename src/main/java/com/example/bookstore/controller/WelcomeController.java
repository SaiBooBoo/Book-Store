package com.example.bookstore.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.LocalDateTime;

@Controller
public class WelcomeController {

    @GetMapping
    public String welcome(Model model) {
        model.addAttribute("message", "Hello, Your Lover, Aik Shen with Thymeleaf!");
        model.addAttribute("today", LocalDateTime.now());
        return "welcome";
    }

    @GetMapping("/home")
    public String home(Model model) {
        return "redirect:admin/books";
    }

}
