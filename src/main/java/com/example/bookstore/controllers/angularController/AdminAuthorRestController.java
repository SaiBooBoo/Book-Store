package com.example.bookstore.controllers.angularController;

import com.example.bookstore.dtos.AuthorDto;
import com.example.bookstore.models.Author;
import com.example.bookstore.services.AuthorService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("api/admin")
public class AdminAuthorRestController {

    private final AuthorService service;

    public AdminAuthorRestController(AuthorService service) {
        this.service = service;
    }

    @GetMapping("/authors")
    public Page<AuthorDto> getAuthorsPaginated(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String direction
    ) {
        return service.findPaginated(page, size, sortBy, direction);
    }

    @GetMapping("/authors/all")
    public ResponseEntity<List<AuthorDto>> getAllAuthors() {
        return ResponseEntity.ok(service.findAllAuthors());
    }

    @PostMapping("/new/author")
    public ResponseEntity<AuthorDto> createAuthor(@Valid @RequestBody AuthorDto authorDto){
        Author savedAuthor = service.save(authorDto);
        authorDto.setId(savedAuthor.getId());
        return ResponseEntity.ok(authorDto);
    }

    @DeleteMapping("/delete/author/{id}")
    public ResponseEntity<Void> deleteAuthor(@PathVariable Long id) {
        service.deleteById(id);
        return ResponseEntity.ok().build();
    }
}
