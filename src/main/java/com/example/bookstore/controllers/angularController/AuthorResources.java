package com.example.bookstore.controllers.angularController;

import com.example.bookstore.criteriaQuery.AuthorQueryCriteria;
import com.example.bookstore.criteriaQuery.Query;
import com.example.bookstore.dtos.AuthorDto;
import com.example.bookstore.dtos.table.DataTableInput;
import com.example.bookstore.dtos.table.DataTableOutput;
import com.example.bookstore.mapper.AuthorMapper;
import com.example.bookstore.models.Author;
import com.example.bookstore.services.AuthorService;
import jakarta.validation.Valid;
import lombok.Builder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

import java.util.Date;
import java.util.List;


@RestController
@RequestMapping("api/admin")
@Builder
public class AuthorResources {

    private final AuthorService service;
    private final AuthorMapper authorMapper;

    @Query(blurry = "name", joinName = "books", propName = "title")
    private String blurry;

    private Pageable pageable;

    private List<Date> dateRange;

    public AuthorResources(AuthorService service, AuthorMapper authorMapper) {
        this.service = service;
        this.authorMapper = authorMapper;
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

   // @PostMapping("/authors/datatable")
//    public ResponseEntity<DataTableOutput<AuthorDto>> authorsDataTable(
//            @RequestBody DataTableInput input
//    ) {
//        DataTableOutput<AuthorDto> output = service.findAuthorsDataTable(input);
//        return ResponseEntity.ok(output);
//    }

    @GetMapping("/authors/all")
    public ResponseEntity<List<AuthorDto>> getAllAuthors() {
        return ResponseEntity.ok(service.findAllAuthors());
    }

    @PostMapping("/author")
    public ResponseEntity<AuthorDto> createAuthor(@Valid @RequestBody AuthorDto authorDto){
        Author savedAuthor = service.save(authorDto);
        authorDto.setId(savedAuthor.getId());
        return ResponseEntity.ok(authorDto);
    }

    @DeleteMapping("/author/{id}")
    public ResponseEntity<Void> deleteAuthor(@PathVariable Long id) {
        service.deleteById(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/authors/datatable")
    public ResponseEntity<DataTableOutput<AuthorDto>> authorsDataTable(
            @RequestBody DataTableInput<AuthorQueryCriteria> input
    ) {
        AuthorQueryCriteria criteria = input.getQueryCriteria();
        criteria.setPageable(input.getPageable());

        Page<Author> page = service.findAuthors(criteria);
        Page<AuthorDto> dtoPage = page.map(authorMapper::toDto);
        return ResponseEntity.ok(DataTableOutput.of(dtoPage, input.getDraw()));
    }

    private AuthorQueryCriteria deserialize(JsonNode jsonNode) {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.convertValue(jsonNode, AuthorQueryCriteria.class);
    }

}
