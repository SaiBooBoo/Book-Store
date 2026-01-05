package com.example.bookstore.services;

import com.example.bookstore.dto.AuthorDetailDto;
import com.example.bookstore.exceptions.AuthorHasBookException;
import com.example.bookstore.exceptions.DuplicateEmailException;
import com.example.bookstore.mapper.AuthorMapper;
import com.example.bookstore.models.Author;
import com.example.bookstore.repositories.AuthorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AuthorService {

    private final AuthorRepository repo;
    private final AuthorMapper mapper;

    public AuthorService(
            AuthorRepository repository, AuthorMapper mapper) {
        this.repo = repository;
        this.mapper = mapper;
    }

    @Transactional(readOnly = true)
    public AuthorDetailDto findById(Long id) {
        Author author = repo.findById(id).orElseThrow(() -> new RuntimeException("Author id not found."));
        return mapper.toDetailDto(author);
    }

    public List<AuthorDetailDto> findAllAuthors() {
        List<Author> authors = repo.findAll();
        return mapper.toDtoList(authors);
    }

    public void save(AuthorDetailDto authorDto)
    {
        Author author = mapper.toAuthorDetail(authorDto);
       if(author.getId() == null) {
           if(repo.existsByEmail(author.getEmail()) || repo.existsByEmailAndIdNot(author.getEmail(), author.getId()) ) {
               throw new DuplicateEmailException("Email already exists");
           }
       }
       repo.save(author);
    }

    @Transactional
    public void deleteById(Long id) {
        Author author = repo.findById(id).orElseThrow(() -> new RuntimeException("Author not found"));

        if (!author.getBooks().isEmpty()) {
            throw new AuthorHasBookException(
                    "Cannot delete author who still has books"
            );
        }
        repo.delete(author);
    }

    public Page<AuthorDetailDto> findPaginated(int page, int size, String sortBy, String direction) {

        Sort sort = direction.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page, size, sort);

        Page<Author> authorPage = repo.findAll(pageable);
        List<AuthorDetailDto> dtoList = mapper.toDtoList(authorPage.getContent());

        return new PageImpl<>(
                dtoList,
                pageable,
                authorPage.getTotalElements()
        );
    }


}
