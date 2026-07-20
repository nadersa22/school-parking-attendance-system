package com.school.parkingattendance.controller;

import com.school.parkingattendance.dto.ApiResponse;
import com.school.parkingattendance.dto.ParkingSpotRequest;
import com.school.parkingattendance.dto.ParkingSpotResponse;
import com.school.parkingattendance.service.ParkingSpotService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/parking-spots")
@RequiredArgsConstructor
public class ParkingSpotController {

    private final ParkingSpotService parkingSpotService;

    @GetMapping
    public List<ParkingSpotResponse> getAllParkingSpots() {
        return parkingSpotService.getAllParkingSpots();
    }

    @GetMapping("/available")
    public List<ParkingSpotResponse> getAvailableParkingSpots() {
        return parkingSpotService.getAvailableParkingSpots();
    }

    @PostMapping
    public ParkingSpotResponse createParkingSpot(@RequestBody ParkingSpotRequest request) {
        return parkingSpotService.createParkingSpot(request);
    }

    @PutMapping("/{id}")
    public ParkingSpotResponse updateParkingSpot(
            @PathVariable Long id,
            @RequestBody ParkingSpotRequest request
    ) {
        return parkingSpotService.updateParkingSpot(id, request);
    }

    @DeleteMapping("/{id}")
    public ApiResponse deleteParkingSpot(@PathVariable Long id) {
        return parkingSpotService.deleteParkingSpot(id);
    }
}