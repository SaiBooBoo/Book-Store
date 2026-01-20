package com.example.bookstore.api.response;

import lombok.Data;

@Data
public class AuthorResponse {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String bio;
}
