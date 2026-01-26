package com.example.bookstore.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig  {

    @Bean
    public PasswordEncoder passwordEncoder() {
            return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {


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
         three ofter
     /*     Angular Security Config */


       http
               .cors(AbstractHttpConfigurer   :: disable)
               .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.POST,"/auth/**").permitAll()
                        .requestMatchers("/login").permitAll()
                        .requestMatchers("/admin/**").hasAnyRole("ADMIN", "USER")
                        .anyRequest().authenticated()
                );

               http.cors(Customizer.withDefaults());

        return http.build();
    }

}
