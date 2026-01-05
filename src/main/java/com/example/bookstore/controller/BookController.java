package com.example.bookstore.controller;

import com.example.bookstore.dto.BookDto;
import com.example.bookstore.models.Book;
import com.example.bookstore.services.AuthorService;
import com.example.bookstore.services.BookService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class BookController {
    @Autowired
    private BookService bookService;

    @Autowired
    private AuthorService authorService;

    @GetMapping("/books/findAll")
    public String findAll(Model model) {
       List<BookDto> books = bookService.findAll();
       model.addAttribute("books", books);
       return "/admin/bookdtoTest";
    }

    @GetMapping("/books/new")
    public String showCreateBookForm(Model model) {
        model.addAttribute("book", new BookDto());
        model.addAttribute("authors", authorService.findAllAuthors());
        return "admin/book-create";
    }

    @GetMapping("/books")
    public String adminBooks(@RequestParam(defaultValue = "0") int page,
                             @RequestParam(defaultValue = "5") int size,
                             @RequestParam(defaultValue = "id") String sortBy,
                             @RequestParam(defaultValue = "asc") String direction,
                             Model model) {

        Page<BookDto> bookPage = bookService.findPaginated(page, size, sortBy, direction);
        model.addAttribute("bookPage", bookPage);
        model.addAttribute("currentPage", page);
        model.addAttribute("size", size);
        model.addAttribute("sortBy", sortBy);
        model.addAttribute("direction", direction);

        return "admin/books";
    }

    @GetMapping("/books/edit/{id}")
    public String editBook(@PathVariable Long id, Model model) {
        BookDto book = bookService.findById(id);
        model.addAttribute("book", book);
        model.addAttribute("authors", authorService.findAllAuthors());
        return "admin/books/book-edit";
    }

    @PostMapping("/books")
    public String createBook(@Valid @ModelAttribute("book") BookDto bookDto, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("authors", authorService.findAllAuthors());
            return "admin/book-create";
        }

        try{
            bookService.createBook(bookDto);
        } catch (RuntimeException ex) {
            result.reject("book.error", ex.getMessage());
            model.addAttribute("authors", authorService.findAllAuthors());
            return "admin/book-create";
        }
        return "redirect:/admin/books";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/books/delete/{id}")
    public String deleteBook(@PathVariable Long id) {
        bookService.deleteById(id);
        return "redirect:/admin/books";
    }

    @PostMapping("/books/edit/{id}")
    public String editBookPost(@PathVariable Long id, Model model) {
        BookDto book = bookService.findById(id);
        model.addAttribute("book", book);
        return "admin/books/book-edit";
    }

    @PostMapping("/books/{id}")
    public String updateBook(@PathVariable Long id,
                             @Valid @ModelAttribute("book") BookDto bookDto,
                             BindingResult result,
                             Model model) {
        if(result.hasErrors()){
            model.addAttribute("authors", authorService.findAllAuthors());
            return "admin/books/book-edit";
        }
        try{
            bookService.updateBook(id, bookDto);
        } catch (IllegalStateException ex){
            result.rejectValue("error.book", ex.getMessage());
            model.addAttribute("authors", authorService.findAllAuthors());
            return "admin/books/book-edit";
        }
        // Exception handling
        return "redirect:/admin/books";
    }
}
