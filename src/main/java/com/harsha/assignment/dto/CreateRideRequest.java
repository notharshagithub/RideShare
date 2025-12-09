package com.harsha.assignment.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateRideRequest {
    
    @NotBlank(message = "Pickup location is required")
    private String pickupLocation;
    
    @NotBlank(message = "Drop location is required")
    private String dropLocation;
    
    @NotNull(message = "Fare amount is required")
    @Positive(message = "Fare amount must be positive")
    private Double fareAmount;
    
    @NotNull(message = "Distance is required")
    @Positive(message = "Distance must be positive")
    private Double distanceKm;
}
