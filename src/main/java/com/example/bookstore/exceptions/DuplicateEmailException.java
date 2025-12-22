package com.example.bookstore.exceptions;

import jakarta.validation.constraints.NotBlank;

public class DuplicateEmailException extends RuntimeException {
    public DuplicateEmailException(String message){
        super(message);
    }
}
