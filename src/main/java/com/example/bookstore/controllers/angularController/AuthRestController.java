package com.example.bookstore.controllers.angularController;

import com.example.bookstore.dtos.UserDto;
import com.example.bookstore.models.User;
import com.example.bookstore.repositories.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("api/auth")
public class AuthRestController {

    private final UserRepository repo;

    public AuthRestController(UserRepository repo) {
        this.repo = repo;
    }

    @GetMapping("/login")
    public ResponseEntity<UserDto> login(@RequestBody User request) {

        User user = repo.findByUsername(request.getUsername())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid username or password"));
        if (!user.getPassword().equals(request.getPassword())){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,
                    "Invalid email or password");
        }
        return ResponseEntity.ok(
                new UserDto(
                       user.getId(),
                       user.getUsername(),
                       user.getRole().name()
               )
        );
    }
}
