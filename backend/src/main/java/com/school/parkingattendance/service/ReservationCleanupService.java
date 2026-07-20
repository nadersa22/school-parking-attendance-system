package com.school.parkingattendance.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.school.parkingattendance.entity.ParkingStatus;
import com.school.parkingattendance.entity.Reservation;
import com.school.parkingattendance.entity.ReservationStatus;
import com.school.parkingattendance.repository.ParkingSpotRepository;
import com.school.parkingattendance.repository.ReservationRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReservationCleanupService {

    private final ReservationRepository reservationRepository;
    private final ParkingSpotRepository parkingSpotRepository;

    @Scheduled(fixedRate = 60000)
    public void expireOldReservations() {

        List<Reservation> expiredReservations =
                reservationRepository.findByStatusAndExpiresAtBefore(
                        ReservationStatus.ACTIVE,
                        LocalDateTime.now()
                );

        for (Reservation reservation : expiredReservations) {
            reservation.setStatus(ReservationStatus.EXPIRED);
            reservation.getParkingSpot().setStatus(ParkingStatus.AVAILABLE);

            parkingSpotRepository.save(reservation.getParkingSpot());
            reservationRepository.save(reservation);
        }

        if (!expiredReservations.isEmpty()) {
            System.out.println("Expired reservations cleaned: " + expiredReservations.size());
        }
    }
}