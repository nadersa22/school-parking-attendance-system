package com.school.parkingattendance.controller;

import com.school.parkingattendance.dto.ApiResponse;
import com.school.parkingattendance.dto.ReservationByTokenRequest;
import com.school.parkingattendance.dto.ReservationResponse;
import com.school.parkingattendance.service.JwtService;
import com.school.parkingattendance.service.ReservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/reservations")
@RequiredArgsConstructor
public class ReservationController {

    private final JwtService jwtService;
    private final ReservationService reservationService;

    @PostMapping("/me")
    public ApiResponse reserveMySpot(
            @RequestHeader("Authorization") String authorizationHeader,
            @RequestBody ReservationByTokenRequest request
    ) {
        String token = authorizationHeader.replace("Bearer ", "");
        Long userId = jwtService.extractUserId(token);

        return reservationService.reserveSpotByUserId(userId, request);
    }

    @GetMapping("/me")
    public List<ReservationResponse> getMyReservations(
            @RequestHeader("Authorization") String authorizationHeader
    ) {
        String token = authorizationHeader.replace("Bearer ", "");
        Long userId = jwtService.extractUserId(token);

        return reservationService.getMyReservations(userId);
    }
}
