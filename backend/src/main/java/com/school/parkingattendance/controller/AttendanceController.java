package com.school.parkingattendance.controller;

import java.util.List;
 
import com.school.parkingattendance.dto.AttendanceResponse;
import com.school.parkingattendance.entity.Attendance;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.school.parkingattendance.service.JwtService;
import com.school.parkingattendance.dto.ApiResponse;
import com.school.parkingattendance.dto.AttendanceRequest;

import com.school.parkingattendance.service.AttendanceService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/attendance")
@RequiredArgsConstructor
public class AttendanceController {
    private final JwtService jwtService;
    private final AttendanceService attendanceService;

    @PostMapping("/check-in")
    public ApiResponse checkIn(@RequestBody AttendanceRequest request) {
        return attendanceService.checkIn(request);
    }

    @PostMapping("/check-out")
    public ApiResponse checkOut(@RequestBody AttendanceRequest request) {
        return attendanceService.checkOut(request);
    }

   @GetMapping
public List<AttendanceResponse> getAllAttendance() {
    return attendanceService.getAllAttendance();
}
    @PostMapping("/me/check-in")
public ApiResponse checkInMe(@RequestHeader("Authorization") String authorizationHeader) {

    String token = authorizationHeader.replace("Bearer ", "");
    Long userId = jwtService.extractUserId(token);

    return attendanceService.checkInByUserId(userId);
}
    @PostMapping("/me/check-out")
public ApiResponse checkOutMe(@RequestHeader("Authorization") String authorizationHeader) {

    String token = authorizationHeader.replace("Bearer ", "");
    Long userId = jwtService.extractUserId(token);

    return attendanceService.checkOutByUserId(userId);
}
   @GetMapping("/me")
public List<AttendanceResponse> getMyAttendanceByToken(
        @RequestHeader("Authorization") String authorizationHeader
) {
    String token = authorizationHeader.replace("Bearer ", "");
    Long userId = jwtService.extractUserId(token);

    return attendanceService.getMyAttendance(userId);
}
    @GetMapping("/my/{userId}")
public List<AttendanceResponse> getMyAttendance(@PathVariable Long userId) {
    return attendanceService.getMyAttendance(userId);
}
}