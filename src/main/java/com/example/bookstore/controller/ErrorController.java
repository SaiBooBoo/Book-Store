package com.example.bookstore.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class ErrorController {

    @PostMapping("/access-denied")
    public String accessDenied(Model model){
        model.addAttribute("errorMessage", "You do not have the authority to delete this resource.");
        return "error/access-denied";
    }
}
