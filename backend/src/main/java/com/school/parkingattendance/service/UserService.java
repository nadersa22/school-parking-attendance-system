package com.school.parkingattendance.service;

import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.school.parkingattendance.dto.ApiResponse;
import com.school.parkingattendance.dto.RegisterRequest;
import com.school.parkingattendance.dto.UserResponse;
import com.school.parkingattendance.entity.Role;
import com.school.parkingattendance.entity.User;
import com.school.parkingattendance.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserResponse addTeacher(RegisterRequest request) {

        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new RuntimeException("Email already exists");
        }

        User user = User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.TEACHER)
                .build();

        User savedUser = userRepository.save(user);

        return mapToResponse(savedUser);
    }

    public List<UserResponse> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    public List<UserResponse> getAllTeachers() {
        return userRepository.findByRole(Role.TEACHER)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    public ApiResponse deleteUser(Long id) {

    if (!userRepository.existsById(id)) {
        return new ApiResponse(false, "User not found");
    }

    userRepository.deleteById(id);

    return new ApiResponse(true, "User deleted successfully");
}

    private UserResponse mapToResponse(User user) {
        return new UserResponse(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getRole()
        );
    }
}