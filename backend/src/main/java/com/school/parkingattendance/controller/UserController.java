package com.school.parkingattendance.controller;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.school.parkingattendance.dto.ApiResponse;
import com.school.parkingattendance.dto.RegisterRequest;
import com.school.parkingattendance.dto.UserResponse;
import com.school.parkingattendance.service.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/teachers")
    public UserResponse addTeacher(@RequestBody RegisterRequest request) {
        return userService.addTeacher(request);
    }

    @GetMapping
    public List<UserResponse> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/teachers")
    public List<UserResponse> getAllTeachers() {
        return userService.getAllTeachers();
    }

    @DeleteMapping("/{id}")
public ApiResponse deleteUser(@PathVariable Long id) {
    return userService.deleteUser(id);
}
}