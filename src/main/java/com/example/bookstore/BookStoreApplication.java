package com.example.bookstore;

import com.example.bookstore.services.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class BookStoreApplication {

    @Bean
    CommandLineRunner init(UserService userService){
        return args -> {
            //userService.createUser("admin", "12345");
        };
    }
    public static void main(String[] args) {
        SpringApplication.run(BookStoreApplication.class, args);


    }

}
