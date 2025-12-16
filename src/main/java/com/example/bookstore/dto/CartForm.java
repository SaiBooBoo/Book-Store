package com.example.bookstore.dto;

import java.util.ArrayList;
import java.util.List;

// src/main/java/.../web/dto/CartForm.java
public class CartForm {

    private List<CartItemForm> items = new ArrayList<>();

    public List<CartItemForm> getItems() {
        return items;
    }

    public void setItems(List<CartItemForm> items) {
        this.items = items;
    }

    // Convert to service-layer DTO
    public List<OrderItemRequest> toOrderItemsRequest() {
        return items.stream()
                .map(i -> new OrderItemRequest(i.getBookId(), i.getQuantity()))
                .toList();
    }
}
