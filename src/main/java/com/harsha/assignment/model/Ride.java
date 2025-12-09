package com.harsha.assignment.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "rides")
public class Ride {
    
    @Id
    private String id;
    
    private String userId; // Passenger
    
    private String driverId; // Driver (nullable)
    
    private String pickupLocation;
    
    private String dropLocation;
    
    private String status; // REQUESTED, ACCEPTED, COMPLETED, CANCELLED
    
    private LocalDateTime createdAt;
    
    private LocalDate createdDate; // For date-based queries
    
    private Double fareAmount; // Fare for the ride
    
    private Double distanceKm; // Distance in kilometers
}
