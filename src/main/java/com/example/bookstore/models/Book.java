package com.example.bookstore.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Setter
@Entity
@Getter
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Title is required!")
    private String title;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id", nullable = false)
    @NotNull(message ="Author is required!")
    private Author author;

    private String isbn;

    @NotNull(message= "Price is required!")
    @DecimalMin(value= "0.0", inclusive = false, message= "Price must be greater than 0")
    private BigDecimal price;

    @NotNull(message= "Stock is required!")
    @DecimalMin(value = "0", message = "Stock must me 0 or more")
    private Integer stock;

    private String description;

}
