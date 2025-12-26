package com.example.bookstore.controller;

import com.example.bookstore.models.User;
import com.example.bookstore.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
@Controller
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping
    public String listUsers(Model model) {
        model.addAttribute("users", userService.getAllUsers());
        return "users/list"; // templates/users/list.html
    }

    @GetMapping("/{id}")
    public String viewUser(@PathVariable Long id, Model model) {
        User user = userService.getUserById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        model.addAttribute("user", user);
        return "users/view"; // templates/users/view.html
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("user", new User());
        return "users/create";
    }

    @PostMapping
    public String createUser(@PathVariable String username, @PathVariable String password) {
        userService.createUser(username, password);
        return "redirect:/users";
    }

    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable Long id, Model model) {
        User user = userService.getUserById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        model.addAttribute("user", user);
        return "users/edit";
    }

    @PostMapping("/{id}")
    public String updateUser(@PathVariable Long id, @ModelAttribute User userDetails) {
        userService.updateUser(id, userDetails);
        return "redirect:/users";
    }

    @PostMapping("/{id}/delete")
    public String deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return "redirect:/users";
    }
}


