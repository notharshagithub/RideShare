package com.harsha.assignment.controller;

import com.harsha.assignment.dto.CreateRideRequest;
import com.harsha.assignment.dto.RideResponse;
import com.harsha.assignment.service.RideService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
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
    
    // ========== NEW ADVANCED QUERY APIs ==========
    
    // API 1: Search rides by pickup OR drop location (keyword search)
    @GetMapping("/rides/search")
    public ResponseEntity<List<RideResponse>> searchRides(@RequestParam String text) {
        List<RideResponse> rides = rideService.searchRides(text);
        return ResponseEntity.ok(rides);
    }
    
    // API 2: Filter rides by distance range
    @GetMapping("/rides/filter-distance")
    public ResponseEntity<List<RideResponse>> filterByDistance(
            @RequestParam Double min,
            @RequestParam Double max) {
        List<RideResponse> rides = rideService.filterByDistance(min, max);
        return ResponseEntity.ok(rides);
    }
    
    // API 3: Filter rides by date range
    @GetMapping("/rides/filter-date-range")
    public ResponseEntity<List<RideResponse>> filterByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end) {
        List<RideResponse> rides = rideService.filterByDateRange(start, end);
        return ResponseEntity.ok(rides);
    }
    
    // API 4: Sort rides by fare (asc/desc)
    @GetMapping("/rides/sort")
    public ResponseEntity<List<RideResponse>> sortByFare(@RequestParam(defaultValue = "asc") String order) {
        List<RideResponse> rides = rideService.sortByFare(order);
        return ResponseEntity.ok(rides);
    }
    
    // API 5: Get all rides for a specific user
    @GetMapping("/rides/user/{userId}")
    public ResponseEntity<List<RideResponse>> getRidesByUserId(@PathVariable String userId) {
        List<RideResponse> rides = rideService.getRidesByUserId(userId);
        return ResponseEntity.ok(rides);
    }
    
    // API 6: Get rides for user filtered by status
    @GetMapping("/rides/user/{userId}/status/{status}")
    public ResponseEntity<List<RideResponse>> getRidesByUserIdAndStatus(
            @PathVariable String userId,
            @PathVariable String status) {
        List<RideResponse> rides = rideService.getRidesByUserIdAndStatus(userId, status);
        return ResponseEntity.ok(rides);
    }
    
    // API 7: Get active rides for a driver
    @GetMapping("/driver/{driverId}/active-rides")
    public ResponseEntity<List<RideResponse>> getActiveRidesForDriver(@PathVariable String driverId) {
        List<RideResponse> rides = rideService.getActiveRidesForDriver(driverId);
        return ResponseEntity.ok(rides);
    }
    
    // API 8: Filter rides by status + keyword search
    @GetMapping("/rides/filter-status")
    public ResponseEntity<List<RideResponse>> filterByStatusAndKeyword(
            @RequestParam String status,
            @RequestParam String search) {
        List<RideResponse> rides = rideService.filterByStatusAndKeyword(status, search);
        return ResponseEntity.ok(rides);
    }
    
    // API 9: Advanced search with keyword, status, sort, and pagination
    @GetMapping("/rides/advanced-search")
    public ResponseEntity<List<RideResponse>> advancedSearch(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String status,
            @RequestParam(defaultValue = "fareAmount") String sort,
            @RequestParam(defaultValue = "asc") String order,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        List<RideResponse> rides = rideService.advancedSearch(search, status, sort, order, page, size);
        return ResponseEntity.ok(rides);
    }
    
    // API 14: Get rides on a specific date
    @GetMapping("/rides/date/{date}")
    public ResponseEntity<List<RideResponse>> getRidesByDate(
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        List<RideResponse> rides = rideService.getRidesByDate(date);
        return ResponseEntity.ok(rides);
    }
}
