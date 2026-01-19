package com.example.bookstore.repositories;

import com.example.bookstore.models.Author;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


import java.util.Optional;

@Repository
public interface AuthorRepository extends JpaRepository<Author, Long>, JpaSpecificationExecutor<Author> {

    @Query("SELECT a FROM Author a WHERE " +
    "LOWER(a.firstName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
    "LOWER(a.lastName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
    "LOWER(a.email) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    Page<Author> searchAuthors(@Param("keyword") String keyword, Pageable pageable);

    Optional<Author> findByEmail(String email);

    boolean existsByEmail(String email);

    boolean existsByEmailAndIdNot(String email, Long id);

    @Query("SELECT a FROM Author a LEFT JOIN FETCH a.books WHERE a.id = :id")
    Optional<Author> findByIdWithBooks(@Param("id") Long id);

    @Query("""
    SELECT a FROM Author a 
    LEFT JOIN FETCH a.books
    WHERE a.id = :id
""")
    Optional<Author> findAuthorWithBooks(@Param("id") Long id);

    @Query("""
    SELECT a FROM Author a 
    WHERE LOWER(a.firstName) LIKE LOWER(CONCAT('%', :search, '%')) OR
    LOWER(a.lastName) LIKE LOWER(CONCAT('%', :search, '%')) OR
    LOWER(a.email) LIKE LOWER(CONCAT('%', :search, '%'))
    """)
    Page<Author> search(@Param("search") String search, Pageable pageable);
}
