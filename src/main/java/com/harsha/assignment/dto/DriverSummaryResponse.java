package com.harsha.assignment.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DriverSummaryResponse {
    private String driverId;
    private Long totalRides;
    private Long completedRides;
    private Long cancelledRides;
    private Double avgDistance;
    private Double totalFare;
}
