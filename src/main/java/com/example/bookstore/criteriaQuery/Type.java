package com.example.bookstore.criteriaQuery;

public enum Type {
    EQUAL,
    NOT_EQUAL,
    IS_NULL,
    NOT_NULL,
    GREATER_THAN,
    GREATER_THAN_OR_EQUAL,
    LESS_THAN,
    LESS_THAN_OR_EQUAL,
    INNER_LIKE,      // %val%
    LEFT_LIKE,       // %val
    RIGHT_LIKE,      // val%
    IN,
    BETWEEN
}
