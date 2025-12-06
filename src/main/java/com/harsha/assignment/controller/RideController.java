package com.harsha.assignment.controller;

import com.harsha.assignment.dto.CreateRideRequest;
import com.harsha.assignment.dto.RideResponse;
import com.harsha.assignment.service.RideService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class RideController {
    
    @Autowired
    private RideService rideService;
    
    // Create a ride (USER only)
    @PostMapping("/rides")
    public ResponseEntity<RideResponse> createRide(@Valid @RequestBody CreateRideRequest request) {
        RideResponse response = rideService.createRide(request);
        return ResponseEntity.ok(response);
    }
    
    // Complete a ride (USER or DRIVER)
    @PostMapping("/rides/{rideId}/complete")
    public ResponseEntity<RideResponse> completeRide(@PathVariable String rideId) {
        RideResponse response = rideService.completeRide(rideId);
        return ResponseEntity.ok(response);
    }
    
    // Get user's own rides (USER only)
    @GetMapping("/user/rides")
    public ResponseEntity<List<RideResponse>> getUserRides() {
        List<RideResponse> rides = rideService.getUserRides();
        return ResponseEntity.ok(rides);
    }
    
    // View pending ride requests (DRIVER only)
    @GetMapping("/driver/rides/requests")
    public ResponseEntity<List<RideResponse>> getPendingRides() {
        List<RideResponse> rides = rideService.getPendingRides();
        return ResponseEntity.ok(rides);
    }
    
    // Accept a ride (DRIVER only)
    @PostMapping("/driver/rides/{rideId}/accept")
    public ResponseEntity<RideResponse> acceptRide(@PathVariable String rideId) {
        RideResponse response = rideService.acceptRide(rideId);
        return ResponseEntity.ok(response);
    }
    
    // Get driver's accepted rides (DRIVER only)
    @GetMapping("/driver/rides")
    public ResponseEntity<List<RideResponse>> getDriverRides() {
        List<RideResponse> rides = rideService.getDriverRides();
        return ResponseEntity.ok(rides);
    }
}
