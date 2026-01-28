package com.example.bookstore.controllers.angularController;

import com.example.bookstore.dtos.login.JwtResponse;
import com.example.bookstore.dtos.login.LoginRequest;
import com.example.bookstore.dtos.login.RegisterRequest;
import com.example.bookstore.models.Role;
import com.example.bookstore.models.User;
import com.example.bookstore.repositories.UserRepository;
import com.example.bookstore.security.CustomUserDetails;
import com.example.bookstore.security.JwtUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthControllerNew {

    private final AuthenticationManager authManager;
    private final UserRepository userRepo;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;

    public AuthControllerNew(AuthenticationManager authManager,
                             UserRepository userRepo,
                             PasswordEncoder passwordEncoder,
                             JwtUtils jwtUtils) {
        this.authManager = authManager;
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtils = jwtUtils;
    }

    @PostMapping("/login")
    public ResponseEntity<JwtResponse> login(@RequestBody LoginRequest req) {
        Authentication auth = authManager.authenticate(
                new UsernamePasswordAuthenticationToken(req.username() , req.password())
        );

        CustomUserDetails userDetails =
                (CustomUserDetails) auth.getPrincipal();

        String token = jwtUtils.generateToken(userDetails);
        long expiresIn = jwtUtils.getExpirationMs();

        return ResponseEntity.ok(new JwtResponse(token, "Bearer", expiresIn));
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest req) {
        if (userRepo.existsByUsername(req.username())) {
            return ResponseEntity.badRequest().body("Username already taken");
        }

        User  u = new User();
        u.setUsername(req.username());
        u.setPassword(passwordEncoder.encode(req.password()));
        u.setRole(Role.ROLE_USER);
        userRepo.save(u);
        return ResponseEntity.ok("User registered");
    }

}
