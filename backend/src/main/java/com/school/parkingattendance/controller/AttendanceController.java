package com.school.parkingattendance.controller;

import com.school.parkingattendance.dto.ApiResponse;
import com.school.parkingattendance.dto.AttendanceResponse;
import com.school.parkingattendance.service.AttendanceService;
import com.school.parkingattendance.service.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/attendance")
@RequiredArgsConstructor
public class AttendanceController {

    private final JwtService jwtService;
    private final AttendanceService attendanceService;

    @GetMapping
    public List<AttendanceResponse> getAllAttendance() {
        return attendanceService.getAllAttendance();
    }

    @PostMapping("/me/check-in")
    public ApiResponse checkInMe(
            @RequestHeader("Authorization") String authorizationHeader
    ) {
        String token = authorizationHeader.replace("Bearer ", "");
        Long userId = jwtService.extractUserId(token);

        return attendanceService.checkInByUserId(userId);
    }

    @PostMapping("/me/check-out")
    public ApiResponse checkOutMe(
            @RequestHeader("Authorization") String authorizationHeader
    ) {
        String token = authorizationHeader.replace("Bearer ", "");
        Long userId = jwtService.extractUserId(token);

        return attendanceService.checkOutByUserId(userId);
    }

    @GetMapping("/me")
    public List<AttendanceResponse> getMyAttendance(
            @RequestHeader("Authorization") String authorizationHeader
    ) {
        String token = authorizationHeader.replace("Bearer ", "");
        Long userId = jwtService.extractUserId(token);

        return attendanceService.getMyAttendance(userId);
    }
}
