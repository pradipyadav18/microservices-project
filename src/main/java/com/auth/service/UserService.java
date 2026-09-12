package com.auth.service;

import com.auth.model.JwtTokenResponse;
import com.auth.model.User;
import com.auth.model.UserDto;
import com.auth.repository.UserRepository;
import com.auth.util.JwtUtil;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    public UserDto save(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        User savedUser = userRepository.save(user);

        return new UserDto(
                savedUser.getId(),
                savedUser.getUsername(),
                savedUser.getEmail(),
                savedUser.getRoles()
        );
    }

    public JwtTokenResponse generateToken(String username) {
        String token = jwtUtil.generateToken(username);

        JwtTokenResponse jwtTokenResponse = new JwtTokenResponse();

        // 1. Actual generated token set karo
        jwtTokenResponse.setToken(token);

        // 2. Token type me "Bearer" set karo (setToken me nahi)
        jwtTokenResponse.setType("Bearer");

        // 3. Expiration time set karo
        jwtTokenResponse.setValidUntil(jwtUtil.extractExpiration(token).toString());

        return jwtTokenResponse;
    }
}