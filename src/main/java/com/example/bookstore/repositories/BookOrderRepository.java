package com.example.bookstore.repositories;

import com.example.bookstore.models.BookOrder;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookOrderRepository extends JpaRepository<BookOrder, Long> {
    List<BookOrder> findByUserId(Long userId);
}
