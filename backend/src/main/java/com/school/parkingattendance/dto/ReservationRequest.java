package com.school.parkingattendance.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReservationRequest {

    private Long userId;
    private Long spotId;
}