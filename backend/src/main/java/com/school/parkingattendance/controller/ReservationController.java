package com.school.parkingattendance.controller;

import java.util.List;

import com.school.parkingattendance.dto.ReservationResponse;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.school.parkingattendance.dto.ReservationByTokenRequest;
import com.school.parkingattendance.service.JwtService;
import com.school.parkingattendance.dto.ApiResponse;
import com.school.parkingattendance.dto.ReservationRequest;
import com.school.parkingattendance.service.ReservationService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/reservations")
@RequiredArgsConstructor
public class ReservationController {
    private final JwtService jwtService;
    private final ReservationService reservationService;
    
    @PostMapping
    public ApiResponse reserveSpot(@RequestBody ReservationRequest request) {
        return reservationService.reserveSpot(request);
    }
    @PostMapping("/me")
public ApiResponse reserveMySpot(
        @RequestHeader("Authorization") String authorizationHeader,
        @RequestBody ReservationByTokenRequest request
) {
    String token = authorizationHeader.replace("Bearer ", "");
    Long userId = jwtService.extractUserId(token);

    return reservationService.reserveSpotByUserId(userId, request);
}
    @GetMapping("/my/{userId}")
public List<ReservationResponse> getMyReservations(@PathVariable Long userId) {
    return reservationService.getMyReservations(userId);
}
   @GetMapping("/me")
public List<ReservationResponse> getMyReservationsByToken(
        @RequestHeader("Authorization") String authorizationHeader
) {
    String token = authorizationHeader.replace("Bearer ", "");
    Long userId = jwtService.extractUserId(token);

    return reservationService.getMyReservations(userId);
}
    @DeleteMapping("/{id}")
    public ApiResponse cancelReservation(@PathVariable Long id) {
        return reservationService.cancelReservation(id);
    }
}