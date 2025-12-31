package com.example.bookstore.services;

import com.example.bookstore.dto.UserDto;
import com.example.bookstore.exceptions.DuplicateEmailException;
import com.example.bookstore.exceptions.DuplicateUsernameException;
import com.example.bookstore.mapper.UserMapper;
import com.example.bookstore.models.Role;
import com.example.bookstore.repositories.UserRepository;
import com.example.bookstore.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper mapper;

    public UserService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder,
                       UserMapper mapper) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.mapper = mapper;
    }

    @Transactional(readOnly = true)
    public List<UserDto> findAll() {
        List<User> users = userRepository.findAll();
        System.out.println(users);
        return mapper.toDtoList(users);
    }

    public void createUser(String username, String password)  {

        if(userRepository.existsByUsername(username)) {
            throw new DuplicateUsernameException("Username already exists. Find another name.");
        }
        User user = new User();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));
        user.setRole(Role.ROLE_USER);

        userRepository.save(user);
    }

    public User updateUser(Long id, User userDetails) {
        User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found with id " + id));
        return userRepository.save(user);
    }

    public void deleteById(Long id) {
        userRepository.deleteById(id);
    }
}
