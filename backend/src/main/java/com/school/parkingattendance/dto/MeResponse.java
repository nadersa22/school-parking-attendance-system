package com.school.parkingattendance.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class MeResponse {

    private Long userId;
    private String email;
    private String role;
}