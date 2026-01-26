package com.example.bookstore.controllers.angularController;

import com.example.bookstore.dtos.UserDto;
import com.example.bookstore.models.User;
import com.example.bookstore.repositories.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("api/auth")
public class AuthRestController {

    private final UserRepository repo;
    private final AuthenticationManager authenticationManager;



    public AuthRestController( AuthenticationManager authenticationManager, UserRepository repo) {
        this.repo = repo;
        this.authenticationManager = authenticationManager;
    }

    @GetMapping("/login")
    public ResponseEntity<UserDto> login(@RequestBody User request) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);

            User user = repo.findByUsername(request.getUsername())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "User record not found after authentication"));

            UserDto dto = new UserDto(user.getId(), user.getUsername(), user.getRole().name());
            return ResponseEntity.ok(dto);

        } catch (AuthenticationException ex) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid username or password");
        }
    }
}
