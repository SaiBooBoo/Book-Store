package com.example.bookstore.dtos.login;

public record LoginResponse (
        String token,
        String username,
        String role
) {}
