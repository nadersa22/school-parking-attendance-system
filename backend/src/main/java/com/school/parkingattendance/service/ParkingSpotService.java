package com.school.parkingattendance.service;

import com.school.parkingattendance.dto.ApiResponse;
import com.school.parkingattendance.dto.ParkingSpotRequest;
import com.school.parkingattendance.dto.ParkingSpotResponse;
import com.school.parkingattendance.entity.ParkingSpot;
import com.school.parkingattendance.entity.ParkingStatus;
import com.school.parkingattendance.repository.ParkingSpotRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ParkingSpotService {

    private final ParkingSpotRepository parkingSpotRepository;

    public List<ParkingSpotResponse> getAllParkingSpots() {
        return parkingSpotRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    public List<ParkingSpotResponse> getAvailableParkingSpots() {
        return parkingSpotRepository.findByStatus(ParkingStatus.AVAILABLE)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    public ParkingSpotResponse createParkingSpot(ParkingSpotRequest request) {

        ParkingSpot parkingSpot = ParkingSpot.builder()
                .spotNumber(request.getSpotNumber())
                .floor(request.getFloor())
                .status(ParkingStatus.AVAILABLE)
                .build();

        ParkingSpot savedSpot = parkingSpotRepository.save(parkingSpot);

        return mapToResponse(savedSpot);
    }

    public ParkingSpotResponse updateParkingSpot(Long id, ParkingSpotRequest request) {

        ParkingSpot parkingSpot = parkingSpotRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Parking spot not found"));

        parkingSpot.setSpotNumber(request.getSpotNumber());
        parkingSpot.setFloor(request.getFloor());

        ParkingSpot updatedSpot = parkingSpotRepository.save(parkingSpot);

        return mapToResponse(updatedSpot);
    }

    public ApiResponse deleteParkingSpot(Long id) {

        if (!parkingSpotRepository.existsById(id)) {
            return new ApiResponse(false, "Parking spot not found");
        }

        parkingSpotRepository.deleteById(id);

        return new ApiResponse(true, "Parking spot deleted successfully");
    }

    private ParkingSpotResponse mapToResponse(ParkingSpot parkingSpot) {
        return new ParkingSpotResponse(
                parkingSpot.getId(),
                parkingSpot.getSpotNumber(),
                parkingSpot.getFloor(),
                parkingSpot.getStatus()
        );
    }
}