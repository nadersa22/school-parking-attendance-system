package com.school.parkingattendance.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.school.parkingattendance.entity.Attendance;
import com.school.parkingattendance.entity.AttendanceStatus;
import com.school.parkingattendance.entity.User;

public interface AttendanceRepository extends JpaRepository<Attendance, Long> {

    List<Attendance> findByUser(User user);

    Optional<Attendance> findByUserAndStatus(User user, AttendanceStatus status);

    long countByCheckInTimeBetween(LocalDateTime start, LocalDateTime end);

    long countByCheckInTimeBetweenAndStatus(
            LocalDateTime start,
            LocalDateTime end,
            AttendanceStatus status
    );

    long countByStatus(AttendanceStatus status);
}