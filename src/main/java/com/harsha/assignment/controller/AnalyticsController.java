package com.harsha.assignment.controller;

import com.harsha.assignment.dto.DriverSummaryResponse;
import com.harsha.assignment.dto.RidesPerDayResponse;
import com.harsha.assignment.dto.StatusSummaryResponse;
import com.harsha.assignment.dto.UserSpendingResponse;
import com.harsha.assignment.service.AnalyticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/analytics")
public class AnalyticsController {

    @Autowired
    private AnalyticsService analyticsService;

    // API 10: Rides per day
    @GetMapping("/rides-per-day")
    public ResponseEntity<List<RidesPerDayResponse>> getRidesPerDay() {
        List<RidesPerDayResponse> response = analyticsService.getRidesPerDay();
        return ResponseEntity.ok(response);
    }

    // API 11: Driver summary (total rides, completed, cancelled, avg distance, total fare)
    @GetMapping("/driver/{driverId}/summary")
    public ResponseEntity<DriverSummaryResponse> getDriverSummary(@PathVariable String driverId) {
        DriverSummaryResponse response = analyticsService.getDriverSummary(driverId);
        return ResponseEntity.ok(response);
    }

    // API 12: User spending (total spent + total completed rides)
    @GetMapping("/user/{userId}/spending")
    public ResponseEntity<UserSpendingResponse> getUserSpending(@PathVariable String userId) {
        UserSpendingResponse response = analyticsService.getUserSpending(userId);
        return ResponseEntity.ok(response);
    }

    // API 13: Status summary (count rides grouped by status)
    @GetMapping("/status-summary")
    public ResponseEntity<List<StatusSummaryResponse>> getStatusSummary() {
        List<StatusSummaryResponse> response = analyticsService.getStatusSummary();
        return ResponseEntity.ok(response);
    }
}
