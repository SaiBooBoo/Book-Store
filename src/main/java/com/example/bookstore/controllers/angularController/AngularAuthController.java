//package com.example.bookstore.controllers.angularController;
//
//import com.example.bookstore.config.CustomUserDetails;
//import com.example.bookstore.config.JwtService;
//import com.example.bookstore.dtos.login.LoginRequest;
//import com.example.bookstore.dtos.login.LoginResponse;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.Authentication;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//@RestController
//@RequestMapping("/api/auth")
//public class AngularAuthController {
//
//    private final AuthenticationManager authenticationManager;
//    private final JwtService jwtService;
//
//    public AngularAuthController(AuthenticationManager authenticationManager, JwtService jwtService) {
//        this.authenticationManager = authenticationManager;
//        this.jwtService = jwtService;
//    }
//
//    public ResponseEntity<LoginResponse> login(
//            @RequestBody LoginRequest request
//            ) {
//        Authentication authentication =
//                authenticationManager.authenticate(
//                        new UsernamePasswordAuthenticationToken(
//                                request.username(),
//                                request.password()
//                        )
//                );
//
//        CustomUserDetails user =
//                (CustomUserDetails) authentication.getPrincipal();
//
//        String jwt = jwtService.generateToken(user);
//
//        return ResponseEntity.ok(
//                new LoginResponse(
//                        jwt,
//                        user.getUsername(),
//                        user.getRole().name()
//                )
//        );
//    }
//}
