package com.example.bookstore.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig  {

    @Bean
    public PasswordEncoder passwordEncoder() {
            return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
                .csrf(csrf -> csrf.disable())
                .cors(Customizer.withDefaults())
                .authorizeHttpRequests(auth -> auth
                                .anyRequest().permitAll()
//                        .requestMatchers("/api/auth/login").permitAll()
//                        .anyRequest().authenticated()
                )
//                .formLogin(form -> form
//                        .loginProcessingUrl("/api/auth/login")
//                        .successHandler((req, res, auth) -> res.setStatus(200))
//                        .failureHandler((req, res, ex) -> res.setStatus(401))
//                )
                .httpBasic(httpBasic -> httpBasic.disable());


        /** Thymeleaf Security Config
         *  http
         *                 .exceptionHandling(exception -> exception.accessDeniedPage("/access-denied"))
         *                 .authorizeHttpRequests(auth -> auth.anyRequest().permitAll()
         *                         .requestMatchers(HttpMethod.POST, "/admin/books/delete/**").hasRole("ADMIN")
         *                 .requestMatchers("/login", "/register", "/webjars/**", "/", "/access-denied", "/users", "/users/**").permitAll()
         *
         *                 .anyRequest().authenticated()
         *         )
         *                 .formLogin(form -> form
         *                         .loginPage("/login")
         *                         .defaultSuccessUrl("/admin/books", true)
         *                         .permitAll()
         *                 )
         *                 .logout(logout -> logout
         *                         .logoutSuccessUrl("/login?logout")); // can route to different places
         */

        return http.build();
    }
}
