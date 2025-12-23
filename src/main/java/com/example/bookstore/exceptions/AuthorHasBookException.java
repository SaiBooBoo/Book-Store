package com.example.bookstore.exceptions;

public class AuthorHasBookException extends RuntimeException{
    public AuthorHasBookException(String message) {
        super(message);
    }
}
