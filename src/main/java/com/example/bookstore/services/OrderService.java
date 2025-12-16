package com.example.bookstore.services;

import com.example.bookstore.dto.OrderItemRequest;
import com.example.bookstore.models.*;
import com.example.bookstore.repositories.BookOrderRepository;
import com.example.bookstore.repositories.BookRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class OrderService {

    @Autowired
    private BookOrderRepository orderRepo;

    @Autowired
    private BookRepository bookRepo;

    @Autowired
    private UserService userService;

    @Transactional
    public BookOrder createOrder(Long userId, List<OrderItemRequest> itemsReq) {
        User user = userService.getUserById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        BookOrder order = new BookOrder();
        order.setUser(user);

        BigDecimal total = BigDecimal.ZERO;
        for (OrderItemRequest req : itemsReq) {
            Book book = bookRepo.findById(req.getBookId())
                    .orElseThrow(() -> new EntityNotFoundException("Book not found"));
            if (book.getStock() < req.getQuantity()) {
                throw new IllegalStateException("Insufficient stock for book " + book.getTitle());
            }

            book.setStock(book.getStock() - req.getQuantity());
            bookRepo.save(book);

            OrderItem item = new OrderItem();
            item.setBook(book);
            item.setQuantity(req.getQuantity());
            item.setPrice(book.getPrice().multiply(BigDecimal.valueOf(req.getQuantity())));
            item.setOrder(order);
            order.getItems().add(item);

            total = total.add(item.getPrice());
        }
        order.setTotal(total);
        return orderRepo.save(order);
    }

    public Optional<BookOrder> getById(Long id) {
        return orderRepo.findById(id);
    }

    public List<BookOrder> getByUser(Long userId) {
        return orderRepo.findByUserId(userId);
    }

    public List<BookOrder> findAll() {
        return orderRepo.findAll();
    }

    @Transactional
    public BookOrder updateStatus(Long orderId, OrderStatus status) {
        BookOrder order = orderRepo.findById(orderId)
                .orElseThrow(() -> new EntityNotFoundException("Order not found"));
        order.setStatus(status);
        return orderRepo.save(order);
    }
}
