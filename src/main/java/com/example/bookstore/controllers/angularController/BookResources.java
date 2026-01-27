package com.example.bookstore.controllers.angularController;

import com.example.bookstore.criteriaQuery.BookQueryCriteria;
import com.example.bookstore.dtos.BookDto;
import com.example.bookstore.dtos.table.DataTableInput;
import com.example.bookstore.dtos.table.DataTableOutput;
import com.example.bookstore.mapper.BookMapper;
import com.example.bookstore.models.Book;
import com.example.bookstore.services.BookService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/admin")
public class BookResources {

    private final BookService service;
    private final BookMapper mapper;
    private final BookMapper bookMapper;

    public BookResources(BookService bookService, BookMapper mapper, BookMapper bookMapper) {
        this.service = bookService;
        this.mapper = mapper;
        this.bookMapper = bookMapper;
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

    @PostMapping("/books")
    public ResponseEntity<BookDto> createBook(@RequestBody BookDto bookDto){
        BookDto savedBook = service.createBook(bookDto);
        return ResponseEntity.ok(savedBook);
    }

    @DeleteMapping("/book/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable Long id){
        service.deleteById(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/book/{id}")
    public ResponseEntity<BookDto> getBook(@PathVariable Long id) {
        Book book = service.getBookById(id);
        BookDto bookDto =  mapper.toDto(book);
        bookDto.setAuthorId(book.getAuthor().getId());
        return ResponseEntity.ok(bookDto);
    }

    @PutMapping("/book/{id}")
    public ResponseEntity<BookDto> updateBook(@PathVariable Long id,@Valid @RequestBody BookDto request) {
          BookDto dto =  service.updateBook(id, request);
          dto.setAuthorId(request.getAuthorId());
        System.out.println(dto.getAuthorId());
        return ResponseEntity.ok(dto);
    }


    @PostMapping("/books/datatable")
    public ResponseEntity<DataTableOutput<BookDto>> booksDataTable (
            @RequestBody DataTableInput<BookQueryCriteria> input
            ) {
        BookQueryCriteria criteria = input.getQueryCriteria();
        criteria.setPageable(input.getPageable());
        Page<Book> page = service.findBooks(criteria);
        Page<BookDto> dtoPage = page.map(bookMapper::toDto);

        return ResponseEntity.ok(DataTableOutput.of(dtoPage, input.getDraw()));

    }
}
