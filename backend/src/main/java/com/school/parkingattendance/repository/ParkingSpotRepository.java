package com.school.parkingattendance.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.school.parkingattendance.entity.ParkingSpot;
import com.school.parkingattendance.entity.ParkingStatus;

public interface ParkingSpotRepository extends JpaRepository<ParkingSpot, Long> {

    List<ParkingSpot> findByStatus(ParkingStatus status);

    long countByStatus(ParkingStatus status);
}