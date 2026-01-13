package com.example.bookstore.controllers.angularController;

import com.example.bookstore.dtos.AuthorDto;
import com.example.bookstore.models.Author;
import com.example.bookstore.services.AuthorService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("api/admin")
public class AdminAuthorRestController {

    private final AuthorService service;

    public AdminAuthorRestController(AuthorService service) {
        this.service = service;
    }

    @GetMapping("/authors")
    public Page<AuthorDto> getAuthors(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String direction
    ) {
        return service.findPaginated(page, size, sortBy, direction);
    }

    @PostMapping("/new/author")
    public ResponseEntity<AuthorDto> createAuthor(@Valid @RequestBody AuthorDto authorDto){
        Author savedAuthor = service.save(authorDto);
        System.out.println(savedAuthor);
        authorDto.setId(savedAuthor.getId());

        return ResponseEntity.ok(authorDto);
    }
}
