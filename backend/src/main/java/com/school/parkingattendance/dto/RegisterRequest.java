package com.school.parkingattendance.dto;

import com.school.parkingattendance.entity.Role;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterRequest {

    private String name;
    private String email;
    private String password;
    private Role role;
}