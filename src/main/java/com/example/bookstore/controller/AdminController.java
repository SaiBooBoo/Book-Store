package com.example.bookstore.controller;

import com.example.bookstore.models.Book;
import com.example.bookstore.models.OrderStatus;
import com.example.bookstore.services.BookService;
import com.example.bookstore.services.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    private BookService bookService;

    @Autowired
    private OrderService orderService;

    public AdminController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping("/orders")
    public String adminOrders(Model model) {
        model.addAttribute("orders", orderService.findAll());
        return "admin/orders";
    }

    @PostMapping("/orders/{id}/status")
    public String changeStatus(@PathVariable Long id, @RequestParam OrderStatus status, RedirectAttributes ra) {
        orderService.updateStatus(id, status);
        ra.addFlashAttribute("message", "Status updated");
        return "redirect:/admin/orders";
    }

    @GetMapping("/books/new")
    public String showCreateBookForm(Model model) {
        model.addAttribute("book", new Book());
        return "admin/book-create";
    }

    @PostMapping("/books")
    public String createBook(@ModelAttribute Book book) {
        bookService.save(book);
        return "redirect:/admin/books";
    }

    @GetMapping("/books")
    public String adminBooks(Model model) {
        model.addAttribute("books", bookService.findAll());
        return "admin/books";
    }

    @GetMapping("/books/edit/{id}")
    public String editBook(@PathVariable Long id, Model model) {
        Book book = bookService.findById(id).orElseThrow();
        model.addAttribute("book", book);
        return "admin/books/book-edit";
    }

    @PostMapping("/books/{id}")
    public String updateBook(@PathVariable Long id, @ModelAttribute Book book) {
        // Ensure the path id is used
        book.setId(id);
        bookService.save(book);
        return "redirect:/admin/books";
    }
}
