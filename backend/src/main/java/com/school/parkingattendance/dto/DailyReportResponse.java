package com.school.parkingattendance.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class DailyReportResponse {

    private long totalAttendanceToday;
    private long validAttendanceToday;
    private long invalidAttendanceToday;
    private long teachersCurrentlyInside;

    private long availableSpots;
    private long reservedSpots;
    private long occupiedSpots;
}