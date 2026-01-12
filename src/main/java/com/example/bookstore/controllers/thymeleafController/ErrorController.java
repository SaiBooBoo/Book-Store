package com.example.bookstore.controllers.thymeleafController;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class ErrorController {

    @PostMapping("/access-denied")
    public String accessDenied(Model model){
        model.addAttribute("errorMessage", "You do not have the authority to delete this resource.");
        return "error/access-denied";
    }
}
