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
@Table(name = "reservations")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Many reservations can belong to one user
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    // Many reservations can be for one parking spot over time
    @ManyToOne
    @JoinColumn(name = "spot_id")
    private ParkingSpot parkingSpot;

    private LocalDateTime reservedAt;

    private LocalDateTime expiresAt;

    @Enumerated(EnumType.STRING)
    private ReservationStatus status;
}