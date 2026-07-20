package com.school.parkingattendance.service;

import java.time.LocalDateTime;
import java.util.List;

import com.school.parkingattendance.dto.ReservationResponse;
import org.springframework.stereotype.Service;
import com.school.parkingattendance.dto.ReservationByTokenRequest;
import com.school.parkingattendance.dto.ApiResponse;
import com.school.parkingattendance.dto.ReservationRequest;
import com.school.parkingattendance.entity.ParkingSpot;
import com.school.parkingattendance.entity.ParkingStatus;
import com.school.parkingattendance.entity.Reservation;
import com.school.parkingattendance.entity.ReservationStatus;
import com.school.parkingattendance.entity.User;
import com.school.parkingattendance.repository.ParkingSpotRepository;
import com.school.parkingattendance.repository.ReservationRepository;
import com.school.parkingattendance.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReservationService {
    
    private final ReservationRepository reservationRepository;
    private final UserRepository userRepository;
    private final ParkingSpotRepository parkingSpotRepository;

    public ApiResponse reserveSpotByUserId(Long userId, ReservationByTokenRequest request) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        ParkingSpot spot = parkingSpotRepository.findById(request.getSpotId())
                .orElseThrow(() -> new RuntimeException("Parking spot not found"));

        if (spot.getStatus() != ParkingStatus.AVAILABLE) {
            return new ApiResponse(false, "Parking spot is not available");
        }

        boolean hasActiveReservation = reservationRepository
                .findByUserAndStatus(user, ReservationStatus.ACTIVE)
                .isPresent();

        if (hasActiveReservation) {
            return new ApiResponse(false, "User already has an active reservation");
        }

        LocalDateTime now = LocalDateTime.now();

        Reservation reservation = Reservation.builder()
                .user(user)
                .parkingSpot(spot)
                .reservedAt(now)
                .expiresAt(now.plusMinutes(15))
                .status(ReservationStatus.ACTIVE)
                .build();

        spot.setStatus(ParkingStatus.RESERVED);

        parkingSpotRepository.save(spot);
        reservationRepository.save(reservation);

        return new ApiResponse(true, "Parking spot reserved successfully");
    }

    public ApiResponse reserveSpot(ReservationRequest request) {

        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        ParkingSpot spot = parkingSpotRepository.findById(request.getSpotId())
                .orElseThrow(() -> new RuntimeException("Parking spot not found"));

        if (spot.getStatus() != ParkingStatus.AVAILABLE) {
            return new ApiResponse(false, "Parking spot is not available");
        }

        boolean hasActiveReservation = reservationRepository
                .findByUserAndStatus(user, ReservationStatus.ACTIVE)
                .isPresent();

        if (hasActiveReservation) {
            return new ApiResponse(false, "User already has an active reservation");
        }

        LocalDateTime now = LocalDateTime.now();

        Reservation reservation = Reservation.builder()
                .user(user)
                .parkingSpot(spot)
                .reservedAt(now)
                .expiresAt(now.plusMinutes(15))
                .status(ReservationStatus.ACTIVE)
                .build();

        spot.setStatus(ParkingStatus.RESERVED);

        parkingSpotRepository.save(spot);
        reservationRepository.save(reservation);

        return new ApiResponse(true, "Parking spot reserved successfully");
    }

    public List<ReservationResponse> getMyReservations(Long userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return reservationRepository.findByUser(user)
                .stream()
                .map(this::mapToReservationResponse)
                .toList();
    }

    public ApiResponse cancelReservation(Long reservationId) {

        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new RuntimeException("Reservation not found"));

        if (reservation.getStatus() != ReservationStatus.ACTIVE) {
            return new ApiResponse(false, "Only active reservations can be cancelled");
        }

        ParkingSpot spot = reservation.getParkingSpot();

        reservation.setStatus(ReservationStatus.CANCELLED);
        spot.setStatus(ParkingStatus.AVAILABLE);

        parkingSpotRepository.save(spot);
        reservationRepository.save(reservation);

        return new ApiResponse(true, "Reservation cancelled successfully");
    }

    private ReservationResponse mapToReservationResponse(Reservation reservation) {
        return new ReservationResponse(
                reservation.getId(),
                reservation.getUser().getName(),
                reservation.getUser().getEmail(),
                reservation.getParkingSpot().getSpotNumber(),
                reservation.getParkingSpot().getFloor(),
                reservation.getReservedAt(),
                reservation.getExpiresAt(),
                reservation.getStatus()
        );
    }
}