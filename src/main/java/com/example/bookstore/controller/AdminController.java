package com.example.bookstore.controller;

import com.example.bookstore.models.Author;
import com.example.bookstore.models.Book;
import com.example.bookstore.services.AuthorService;
import com.example.bookstore.services.BookService;
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
    private AuthorService authorService;

    public AdminController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping("/books/new")
    public String showCreateBookForm(Model model) {
        model.addAttribute("book", new Book());
        model.addAttribute("authors", authorService.findAllAuthors());
        return "admin/book-create";
    }

    @GetMapping("/authors/new")
    public String showCreateAuthorForm(Model model) {
        model.addAttribute("author", new Author());
        return "admin/author-create";
    }

    @PostMapping("/books/edit/{id}")
    public String editBookPost(@PathVariable Long id, Model model) {
        Book book = bookService.findById(id).orElseThrow();
        model.addAttribute("book", book);
        return "admin/books/book-edit";
    }
}
