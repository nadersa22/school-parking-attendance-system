package com.school.parkingattendance.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ParkingSpotRequest {

    private String spotNumber;
    private String floor;
}