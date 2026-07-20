package com.school.parkingattendance.service;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import com.school.parkingattendance.dto.DailyReportResponse;
import com.school.parkingattendance.entity.AttendanceStatus;
import com.school.parkingattendance.entity.ParkingStatus;
import com.school.parkingattendance.repository.AttendanceRepository;
import com.school.parkingattendance.repository.ParkingSpotRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReportService {

    private final AttendanceRepository attendanceRepository;
    private final ParkingSpotRepository parkingSpotRepository;

    public DailyReportResponse getDailyReport() {

        LocalDate today = LocalDate.now();

        LocalDateTime startOfDay = today.atStartOfDay();
        LocalDateTime endOfDay = today.plusDays(1).atStartOfDay();

        long totalAttendanceToday =
                attendanceRepository.countByCheckInTimeBetween(startOfDay, endOfDay);

        long validAttendanceToday =
                attendanceRepository.countByCheckInTimeBetweenAndStatus(
                        startOfDay,
                        endOfDay,
                        AttendanceStatus.VALID
                );

        long invalidAttendanceToday =
                attendanceRepository.countByCheckInTimeBetweenAndStatus(
                        startOfDay,
                        endOfDay,
                        AttendanceStatus.INVALID
                );

        long teachersCurrentlyInside =
                attendanceRepository.countByStatus(AttendanceStatus.IN_PROGRESS);

        long availableSpots =
                parkingSpotRepository.countByStatus(ParkingStatus.AVAILABLE);

        long reservedSpots =
                parkingSpotRepository.countByStatus(ParkingStatus.RESERVED);

        long occupiedSpots =
                parkingSpotRepository.countByStatus(ParkingStatus.OCCUPIED);

        return new DailyReportResponse(
                totalAttendanceToday,
                validAttendanceToday,
                invalidAttendanceToday,
                teachersCurrentlyInside,
                availableSpots,
                reservedSpots,
                occupiedSpots
        );
    }
}