package com.school.parkingattendance.controller;

import com.school.parkingattendance.dto.AuthResponse;
import com.school.parkingattendance.dto.LoginRequest;
import com.school.parkingattendance.dto.MeResponse;
import com.school.parkingattendance.dto.RegisterRequest;
import com.school.parkingattendance.service.AuthService;
import com.school.parkingattendance.service.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final JwtService jwtService;

    @PostMapping("/register")
    public AuthResponse register(@RequestBody RegisterRequest request) {
        return authService.register(request);
    }

    @PostMapping("/login")
    public AuthResponse login(@RequestBody LoginRequest request) {
        return authService.login(request);
    }

    @GetMapping("/me")
    public MeResponse getCurrentUser(@RequestHeader("Authorization") String authorizationHeader) {

        String token = authorizationHeader.replace("Bearer ", "");

        return new MeResponse(
                jwtService.extractUserId(token),
                jwtService.extractEmail(token),
                jwtService.extractRole(token)
        );
    }
}