package com.school.parkingattendance.service;

import com.school.parkingattendance.dto.AuthResponse;
import com.school.parkingattendance.dto.LoginRequest;
import com.school.parkingattendance.dto.RegisterRequest;
import com.school.parkingattendance.entity.User;
import com.school.parkingattendance.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthResponse register(RegisterRequest request) {

        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            return new AuthResponse("Email already exists", null, null);
        }

        User user = User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(request.getRole())
                .build();

        User savedUser = userRepository.save(user);

        String token = jwtService.generateToken(savedUser);

        return new AuthResponse(
                "User registered successfully",
                savedUser.getRole().name(),
                token
        );
    }

    public AuthResponse login(LoginRequest request) {

        User user = userRepository.findByEmail(request.getEmail())
                .orElse(null);

        if (user == null) {
            return new AuthResponse("Invalid email or password", null, null);
        }

        boolean passwordMatches = passwordEncoder.matches(
                request.getPassword(),
                user.getPassword()
        );

        if (!passwordMatches) {
            return new AuthResponse("Invalid email or password", null, null);
        }

        String token = jwtService.generateToken(user);

        return new AuthResponse(
                "Login successful",
                user.getRole().name(),
                token
        );
    }
}