package com.example.bookstore.services;

import com.example.bookstore.models.Book;
import com.example.bookstore.repositories.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BookService {

    @Autowired
    private BookRepository repo;

    public Page<Book> findAll(Pageable pageable) {
        return repo.findAll(pageable);
    }

    public Optional<Book> findById(Long id) {
        return repo.findById(id);
    }

    public Book save(Book b) {
        return repo.save(b);
    }

    public void deleteById(Long id) {
        repo.deleteById(id);
    }

    public Page<Book> search(String keyword, Pageable pageable) {
        return repo
                .findByTitleContainingIgnoreCaseOrAuthorContainingIgnoreCase(keyword, keyword, pageable);
    }

    public Page<Book> findPaginated(int page, int size, String sortBy, String direction) {

        Sort sort = direction.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page, size, sort);
        return repo.findAll(pageable);
    }
}
