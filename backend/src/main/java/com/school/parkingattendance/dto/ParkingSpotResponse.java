package com.school.parkingattendance.dto;

import com.school.parkingattendance.entity.ParkingStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ParkingSpotResponse {

    private Long id;
    private String spotNumber;
    private String floor;
    private ParkingStatus status;
}