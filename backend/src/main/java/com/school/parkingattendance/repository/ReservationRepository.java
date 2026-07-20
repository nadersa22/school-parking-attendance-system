package com.school.parkingattendance.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.school.parkingattendance.entity.Reservation;
import com.school.parkingattendance.entity.ReservationStatus;
import com.school.parkingattendance.entity.User;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    List<Reservation> findByUser(User user);

    Optional<Reservation> findByUserAndStatus(User user, ReservationStatus status);

    List<Reservation> findByStatusAndExpiresAtBefore(
            ReservationStatus status,
            LocalDateTime now
    );
}