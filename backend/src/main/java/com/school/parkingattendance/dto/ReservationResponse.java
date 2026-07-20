package com.school.parkingattendance.dto;

import com.school.parkingattendance.entity.ReservationStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class ReservationResponse {

    private Long id;

    private String teacherName;
    private String teacherEmail;

    private String spotNumber;
    private String floor;

    private LocalDateTime reservedAt;
    private LocalDateTime expiresAt;

    private ReservationStatus status;
}