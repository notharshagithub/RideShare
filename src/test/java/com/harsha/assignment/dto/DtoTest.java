package com.harsha.assignment.dto;

import com.harsha.assignment.model.Ride;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class DtoTest {

    @Test
    void testCreateRideRequest() {
        CreateRideRequest request = new CreateRideRequest();
        request.setPickupLocation("Location A");
        request.setDropLocation("Location B");
        request.setFareAmount(100.0);
        request.setDistanceKm(10.0);

        assertEquals("Location A", request.getPickupLocation());
        assertEquals("Location B", request.getDropLocation());
        assertEquals(100.0, request.getFareAmount());
        assertEquals(10.0, request.getDistanceKm());
    }

    @Test
    void testRideResponseFromRide() {
        Ride ride = new Ride();
        ride.setId("ride123");
        ride.setUserId("user123");
        ride.setDriverId("driver123");
        ride.setPickupLocation("Location A");
        ride.setDropLocation("Location B");
        ride.setStatus("REQUESTED");
        ride.setCreatedAt(LocalDateTime.now());
        ride.setCreatedDate(LocalDate.now());
        ride.setFareAmount(100.0);
        ride.setDistanceKm(10.0);

        RideResponse response = RideResponse.fromRide(ride);

        assertEquals("ride123", response.getId());
        assertEquals("user123", response.getUserId());
        assertEquals("driver123", response.getDriverId());
        assertEquals("Location A", response.getPickupLocation());
        assertEquals("Location B", response.getDropLocation());
        assertEquals("REQUESTED", response.getStatus());
        assertEquals(100.0, response.getFareAmount());
        assertEquals(10.0, response.getDistanceKm());
        assertNotNull(response.getCreatedAt());
        assertNotNull(response.getCreatedDate());
    }

    @Test
    void testLoginRequest() {
        LoginRequest request = new LoginRequest();
        request.setUsername("testuser");
        request.setPassword("password");

        assertEquals("testuser", request.getUsername());
        assertEquals("password", request.getPassword());
    }

    @Test
    void testRegisterRequest() {
        RegisterRequest request = new RegisterRequest();
        request.setUsername("testuser");
        request.setPassword("password");
        request.setRole("ROLE_USER");

        assertEquals("testuser", request.getUsername());
        assertEquals("password", request.getPassword());
        assertEquals("ROLE_USER", request.getRole());
    }

    @Test
    void testAuthResponse() {
        AuthResponse response = new AuthResponse("token123", "testuser", "ROLE_USER");

        assertEquals("token123", response.getToken());
        assertEquals("testuser", response.getUsername());
        assertEquals("ROLE_USER", response.getRole());
    }

    @Test
    void testRidesPerDayResponse() {
        LocalDate date = LocalDate.now();
        RidesPerDayResponse response = new RidesPerDayResponse(date, 10L);

        assertEquals(date, response.getDate());
        assertEquals(10L, response.getCount());
    }

    @Test
    void testDriverSummaryResponse() {
        DriverSummaryResponse response = new DriverSummaryResponse(
            "driver123", 100L, 90L, 10L, 15.5, 25000.0
        );

        assertEquals("driver123", response.getDriverId());
        assertEquals(100L, response.getTotalRides());
        assertEquals(90L, response.getCompletedRides());
        assertEquals(10L, response.getCancelledRides());
        assertEquals(15.5, response.getAvgDistance());
        assertEquals(25000.0, response.getTotalFare());
    }

    @Test
    void testUserSpendingResponse() {
        UserSpendingResponse response = new UserSpendingResponse("user123", 50L, 12500.0);

        assertEquals("user123", response.getUserId());
        assertEquals(50L, response.getTotalCompletedRides());
        assertEquals(12500.0, response.getTotalSpent());
    }

    @Test
    void testStatusSummaryResponse() {
        StatusSummaryResponse response = new StatusSummaryResponse("COMPLETED", 100L);

        assertEquals("COMPLETED", response.getStatus());
        assertEquals(100L, response.getCount());
    }

    @Test
    void testCreateRideRequestAllArgsConstructor() {
        CreateRideRequest request = new CreateRideRequest("Location A", "Location B", 100.0, 10.0);

        assertEquals("Location A", request.getPickupLocation());
        assertEquals("Location B", request.getDropLocation());
        assertEquals(100.0, request.getFareAmount());
        assertEquals(10.0, request.getDistanceKm());
    }

    @Test
    void testRideResponseAllFields() {
        LocalDateTime now = LocalDateTime.now();
        LocalDate today = LocalDate.now();
        
        RideResponse response = new RideResponse("ride123", "user123", "driver123", 
            "Location A", "Location B", "REQUESTED", now, today, 100.0, 10.0);

        assertNotNull(response.getId());
        assertNotNull(response.getUserId());
        assertNotNull(response.getPickupLocation());
        assertNotNull(response.getStatus());
        assertNotNull(response.getFareAmount());
    }
}
