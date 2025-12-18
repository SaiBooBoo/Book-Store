package com.example.bookstore.services;

import com.example.bookstore.models.Author;
import com.example.bookstore.repositories.AuthorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AuthorService {

    @Autowired
    private AuthorRepository repo;

    public Page<Author> findAll(Pageable pageable) {
        return repo.findAll(pageable);
    }

    public Optional<Author> findById(Long id) {
        return repo.findById(id);
    }

    public List<Author> findAllAuthors() {return repo.findAll();}

    public Author save(Author a)
    {
        return repo.save(a);
    }

    public void deleteById(Long id) {
        repo.deleteById(id);
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
