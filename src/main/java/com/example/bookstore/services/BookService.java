package com.example.bookstore.services;

import com.example.bookstore.models.Book;
import com.example.bookstore.repositories.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BookService {

    @Autowired
    private BookRepository repo;

    public List<Book> findAll() {
        return repo.findAll();
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

    public List<Book> search(String keyword) {
        return repo
                .findByTitleContainingIgnoreCaseOrAuthorContainingIgnoreCase(keyword, keyword);
    }
}
