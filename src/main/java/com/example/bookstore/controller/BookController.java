package com.example.bookstore.controller;

import com.example.bookstore.models.Book;
import com.example.bookstore.repositories.BookRepository;
import com.example.bookstore.services.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Controller
@RequestMapping("/books")
public class BookController {
    @Autowired
    private BookService bookService;

    @GetMapping
    public String listBooks(
            @RequestParam(required=false) String q,
            Pageable pageable, Model model) {
        Page<Book> bookPage = (q == null || q.isBlank())
                ? bookService.findAll(pageable)
                : bookService.search(q, pageable);
        model.addAttribute("bookPage", bookPage);
        model.addAttribute("q", q);
        return "books/list"; // src/main/resources/templates/books/list.html
    }

    @GetMapping("/{id}")
    public String details(@PathVariable Long id, Model model) {
        Book book = bookService.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        model.addAttribute("book", book);
        return "books/details";

    }

}
