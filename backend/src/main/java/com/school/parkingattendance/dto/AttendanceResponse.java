package com.school.parkingattendance.dto;

import com.school.parkingattendance.entity.AttendanceStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class AttendanceResponse {

    private Long id;

    private String teacherName;
    private String teacherEmail;

    private String spotNumber;
    private String floor;

    private LocalDateTime checkInTime;
    private LocalDateTime checkOutTime;

    private Long durationMinutes;

    private AttendanceStatus status;
}