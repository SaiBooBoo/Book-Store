package com.example.bookstore.repositories;

import com.example.bookstore.models.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BookRepository extends JpaRepository<Book, Long> {
    List<Book> findByTitleContainingIgnoreCase(String q);

    Page<Book> findByTitleContainingIgnoreCaseOrAuthorContainingIgnoreCase(
            String title,
            String author,
            Pageable pageable
    );

    @Query("""
            SELECT b FROM Book b
            WHERE LOWER(b.title) LIKE LOWER(CONCAT('%', :searchValue, '%'))
            OR LOWER(b.author.firstName) LIKE LOWER(CONCAT('%', :searchValue, '%'))
            OR LOWER(b.author.lastName) LIKE LOWER(CONCAT('%', :searchValue, '%'))
    """)
    Page<Book> search(@Param("searchValue") String searchValue, Pageable pageable);
}
