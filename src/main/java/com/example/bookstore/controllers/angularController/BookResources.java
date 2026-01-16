package com.example.bookstore.controllers.angularController;

import com.example.bookstore.dtos.BookDto;
import com.example.bookstore.services.BookService;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("api/admin")
public class BookResources {

    private final BookService service;

    public BookResources(BookService bookService) {
        this.service = bookService;
    }

    @GetMapping("/books")
    public Page<BookDto> getBooksPaginated(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String direction
    ) {
        return service.findPaginated(page,size, sortBy, direction);
    }

//    @PostMapping("/books/datatable")
//    public DataTableOutput<BookDto> getBooks(
//            @RequestBody DataTableInput<BookDto> input
//            ) {
//        return service.findBooksDataTable(input);
//    }

    @PostMapping("/book")
    public ResponseEntity<BookDto> createBook(@RequestBody BookDto bookDto){
        BookDto savedBook = service.createBook(bookDto);
        return ResponseEntity.ok(savedBook);
    }

    @DeleteMapping("/book/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable Long id){
        service.deleteById(id);
        return ResponseEntity.ok().build();
    }

}
