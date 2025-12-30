package com.example.bookstore;

import com.example.bookstore.repositories.UserRepository;
import com.example.bookstore.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.transaction.annotation.Transactional;

@SpringBootApplication
@RequiredArgsConstructor
public class BookStoreApplication {

    private final UserRepository userRepo;

    @Bean @Transactional
    CommandLineRunner init(UserService userService){
        return args -> {

        };
    }


    public static void main(String[] args) {
        SpringApplication.run(BookStoreApplication.class, args);


    }

}
