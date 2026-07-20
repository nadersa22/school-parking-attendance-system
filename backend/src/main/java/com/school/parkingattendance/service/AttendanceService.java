package com.school.parkingattendance.service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import com.school.parkingattendance.dto.AttendanceResponse;
import org.springframework.stereotype.Service;

import com.school.parkingattendance.dto.ApiResponse;
import com.school.parkingattendance.dto.AttendanceRequest;
import com.school.parkingattendance.entity.Attendance;
import com.school.parkingattendance.entity.AttendanceStatus;
import com.school.parkingattendance.entity.ParkingSpot;
import com.school.parkingattendance.entity.ParkingStatus;
import com.school.parkingattendance.entity.Reservation;
import com.school.parkingattendance.entity.ReservationStatus;
import com.school.parkingattendance.entity.User;
import com.school.parkingattendance.repository.AttendanceRepository;
import com.school.parkingattendance.repository.ParkingSpotRepository;
import com.school.parkingattendance.repository.ReservationRepository;
import com.school.parkingattendance.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AttendanceService {

    private final AttendanceRepository attendanceRepository;
    private final ReservationRepository reservationRepository;
    private final UserRepository userRepository;
    private final ParkingSpotRepository parkingSpotRepository;

    public ApiResponse checkOutByUserId(Long userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Attendance attendance = attendanceRepository
                .findByUserAndStatus(user, AttendanceStatus.IN_PROGRESS)
                .orElse(null);

        if (attendance == null) {
            return new ApiResponse(false, "No active check-in found");
        }

        LocalDateTime checkOutTime = LocalDateTime.now();

        long durationMinutes = Duration.between(
                attendance.getCheckInTime(),
                checkOutTime
        ).toMinutes();

        AttendanceStatus finalStatus;

        if (durationMinutes >= 240) {
            finalStatus = AttendanceStatus.VALID;
        } else {
            finalStatus = AttendanceStatus.INVALID;
        }

        ParkingSpot spot = attendance.getParkingSpot();
        spot.setStatus(ParkingStatus.AVAILABLE);

        attendance.setCheckOutTime(checkOutTime);
        attendance.setDurationMinutes(durationMinutes);
        attendance.setStatus(finalStatus);

        parkingSpotRepository.save(spot);
        attendanceRepository.save(attendance);

        return new ApiResponse(
                true,
                "Check-out successful. Duration: " + durationMinutes + " minutes. Status: " + finalStatus
        );
    }

    public ApiResponse checkInByUserId(Long userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Reservation reservation = reservationRepository
                .findByUserAndStatus(user, ReservationStatus.ACTIVE)
                .orElse(null);

        if (reservation == null) {
            return new ApiResponse(false, "No active reservation found");
        }

        if (LocalDateTime.now().isAfter(reservation.getExpiresAt())) {
            reservation.setStatus(ReservationStatus.EXPIRED);
            reservation.getParkingSpot().setStatus(ParkingStatus.AVAILABLE);

            parkingSpotRepository.save(reservation.getParkingSpot());
            reservationRepository.save(reservation);

            return new ApiResponse(false, "Reservation expired. Please reserve again.");
        }

        boolean alreadyCheckedIn = attendanceRepository
                .findByUserAndStatus(user, AttendanceStatus.IN_PROGRESS)
                .isPresent();

        if (alreadyCheckedIn) {
            return new ApiResponse(false, "User already checked in");
        }

        ParkingSpot spot = reservation.getParkingSpot();
        spot.setStatus(ParkingStatus.OCCUPIED);
        reservation.setStatus(ReservationStatus.COMPLETED);

        Attendance attendance = Attendance.builder()
                .user(user)
                .parkingSpot(spot)
                .checkInTime(LocalDateTime.now())
                .status(AttendanceStatus.IN_PROGRESS)
                .build();

        parkingSpotRepository.save(spot);
        reservationRepository.save(reservation);
        attendanceRepository.save(attendance);

        return new ApiResponse(true, "Check-in successful");
    }

    public ApiResponse checkIn(AttendanceRequest request) {

        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Reservation reservation = reservationRepository
                .findByUserAndStatus(user, ReservationStatus.ACTIVE)
                .orElse(null);

        if (reservation == null) {
            return new ApiResponse(false, "No active reservation found");
        }

        if (LocalDateTime.now().isAfter(reservation.getExpiresAt())) {
            reservation.setStatus(ReservationStatus.EXPIRED);
            reservation.getParkingSpot().setStatus(ParkingStatus.AVAILABLE);

            parkingSpotRepository.save(reservation.getParkingSpot());
            reservationRepository.save(reservation);

            return new ApiResponse(false, "Reservation expired. Please reserve again.");
        }

        boolean alreadyCheckedIn = attendanceRepository
                .findByUserAndStatus(user, AttendanceStatus.IN_PROGRESS)
                .isPresent();

        if (alreadyCheckedIn) {
            return new ApiResponse(false, "User already checked in");
        }

        ParkingSpot spot = reservation.getParkingSpot();
        spot.setStatus(ParkingStatus.OCCUPIED);
        reservation.setStatus(ReservationStatus.COMPLETED);

        Attendance attendance = Attendance.builder()
                .user(user)
                .parkingSpot(spot)
                .checkInTime(LocalDateTime.now())
                .status(AttendanceStatus.IN_PROGRESS)
                .build();

        parkingSpotRepository.save(spot);
        reservationRepository.save(reservation);
        attendanceRepository.save(attendance);

        return new ApiResponse(true, "Check-in successful");
    }

    public ApiResponse checkOut(AttendanceRequest request) {

        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Attendance attendance = attendanceRepository
                .findByUserAndStatus(user, AttendanceStatus.IN_PROGRESS)
                .orElse(null);

        if (attendance == null) {
            return new ApiResponse(false, "No active check-in found");
        }

        LocalDateTime checkOutTime = LocalDateTime.now();

        long durationMinutes = Duration.between(
                attendance.getCheckInTime(),
                checkOutTime
        ).toMinutes();

        AttendanceStatus finalStatus;

        if (durationMinutes >= 240) {
            finalStatus = AttendanceStatus.VALID;
        } else {
            finalStatus = AttendanceStatus.INVALID;
        }

        ParkingSpot spot = attendance.getParkingSpot();
        spot.setStatus(ParkingStatus.AVAILABLE);

        attendance.setCheckOutTime(checkOutTime);
        attendance.setDurationMinutes(durationMinutes);
        attendance.setStatus(finalStatus);

        parkingSpotRepository.save(spot);
        attendanceRepository.save(attendance);

        return new ApiResponse(
                true,
                "Check-out successful. Duration: " + durationMinutes + " minutes. Status: " + finalStatus
        );
    }

    public List<AttendanceResponse> getAllAttendance() {
        return attendanceRepository.findAll()
                .stream()
                .map(this::mapToAttendanceResponse)
                .toList();
    }

    public List<AttendanceResponse> getMyAttendance(Long userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return attendanceRepository.findByUser(user)
                .stream()
                .map(this::mapToAttendanceResponse)
                .toList();
    }

    private AttendanceResponse mapToAttendanceResponse(Attendance attendance) {
        return new AttendanceResponse(
                attendance.getId(),
                attendance.getUser().getName(),
                attendance.getUser().getEmail(),
                attendance.getParkingSpot().getSpotNumber(),
                attendance.getParkingSpot().getFloor(),
                attendance.getCheckInTime(),
                attendance.getCheckOutTime(),
                attendance.getDurationMinutes(),
                attendance.getStatus()
        );
    }
}