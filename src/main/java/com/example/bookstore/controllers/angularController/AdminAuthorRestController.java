package com.example.bookstore.controllers.angularController;

import com.example.bookstore.dto.AuthorDto;
import com.example.bookstore.services.AuthorService;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
}
