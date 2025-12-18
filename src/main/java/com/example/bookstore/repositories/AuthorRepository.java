package com.example.bookstore.repositories;

import com.example.bookstore.models.Author;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthorRepository extends JpaRepository<Author, Long> {

    @Query("SELECT a FROM Author a WHERE " +
    "LOWER(a.firstName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
    "LOWER(a.lastName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
    "LOWER(a.email) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    Page<Author> searchAuthors(@Param("keyword") String keyword, Pageable pageable);

}
