package com.school.parkingattendance.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "attendance")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Attendance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // The teacher/user who checked in
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    // The parking spot used
    @ManyToOne
    @JoinColumn(name = "spot_id")
    private ParkingSpot parkingSpot;

    private LocalDateTime checkInTime;

    private LocalDateTime checkOutTime;

    private Long durationMinutes;

    @Enumerated(EnumType.STRING)
    private AttendanceStatus status;
}