package com.example.bookstore.services;

import com.example.bookstore.dto.AuthorDetailDto;
import com.example.bookstore.exceptions.AuthorHasBookException;
import com.example.bookstore.exceptions.DuplicateEmailException;
import com.example.bookstore.mapper.AuthorMapper;
import com.example.bookstore.models.Author;
import com.example.bookstore.repositories.AuthorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.webjars.NotFoundException;

import java.util.List;

@Service
public class AuthorService {

    @Autowired
    private AuthorRepository repo;
    private AuthorMapper mapper;


    public AuthorService(AuthorRepository repository) {
        this.repo = repository;
    }

    @Transactional(readOnly = true)
    public Page<Author> findAll(Pageable pageable) {
        return repo.findAll(pageable);
    }

    @Transactional(readOnly = true)
    public AuthorDetailDto findById(Long id) {

        return mapper.toDetailDto(repo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Author not found")));
    }

    @Transactional(readOnly = true)
    public AuthorDetailDto findAuthorDetail(Long id) {
        Author author = repo.findByIdWithBooks(id)
                .orElseThrow(() -> new NotFoundException("Author not found"));
        return mapper.toDetailDto(author);
    }

    public List<Author> findAllAuthors() {return repo.findAll();}

    public void save(Author author)
    {
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

    public Page<Author> search(String keyword, Pageable pageable) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return repo.findAll(pageable);
        }
        return repo.searchAuthors(keyword, pageable);
    }

    public Page<Author> findPaginated(int page, int size, String sortBy, String direction) {

        Sort sort = direction.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page, size, sort);
        return repo.findAll(pageable);
    }


}
