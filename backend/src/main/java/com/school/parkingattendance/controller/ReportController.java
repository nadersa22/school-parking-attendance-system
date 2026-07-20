package com.school.parkingattendance.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.school.parkingattendance.dto.DailyReportResponse;
import com.school.parkingattendance.service.ReportService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/reports")
@RequiredArgsConstructor
public class ReportController {

    private final ReportService reportService;

    @GetMapping("/daily")
    public DailyReportResponse getDailyReport() {
        return reportService.getDailyReport();
    }
}