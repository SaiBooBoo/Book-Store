package com.example.bookstore.dtos.login;

public record JwtResponse (
        String token,
        String tokenType,
        Long expiresIn) {
}
