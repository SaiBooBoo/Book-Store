package com.example.bookstore.dto;

// src/main/java/.../web/dto/CartItemForm.java
public class CartItemForm {

    private Long bookId;
    private Integer quantity;

    public Long getBookId() {
        return bookId;
    }

    public void setBookId(Long bookId) {
        this.bookId = bookId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}
