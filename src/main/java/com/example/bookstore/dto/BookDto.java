package com.example.bookstore.dto;


import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;


public class BookDto {

    private Long id;
    @NotBlank(message = "Title is required!")
    private String title;
    private String isbn;
    @NotNull(message= "Price is required!")
    @DecimalMin(value= "0.0", inclusive = false, message= "Price must be greater than 0")
    private BigDecimal price;
    @NotNull(message= "Stock is required!")
    @DecimalMin(value = "0", message = "Stock must me 0 or more")
    private Integer stock;
    private String description;

    private String authorName;
    @NotNull(message ="Author is required!")
    private Long authorId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public Long getAuthorId() {
        return authorId;
    }

    public void setAuthorId(Long authorId) {
        this.authorId = authorId;
    }
}
