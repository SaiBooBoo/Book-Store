package com.example.bookstore.controllers.angularController;

import com.example.bookstore.dto.BookDto;
import com.example.bookstore.services.BookService;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/admin")
public class AdminBookRestController {

    private final BookService bookService;

    public AdminBookRestController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping("/books")
    public Page<BookDto> getBooks(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String direction
    ) {
        return bookService.findPaginated(page,size, sortBy, direction);
    }

}
