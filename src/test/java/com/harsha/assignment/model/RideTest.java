package com.harsha.assignment.model;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class RideTest {

    @Test
    void testRideCreation() {
        Ride ride = new Ride();
        ride.setId("ride123");
        ride.setUserId("user123");
        ride.setDriverId("driver123");
        ride.setPickupLocation("Location A");
        ride.setDropLocation("Location B");
        ride.setStatus("REQUESTED");
        ride.setFareAmount(100.0);
        ride.setDistanceKm(10.0);
        ride.setCreatedAt(LocalDateTime.now());
        ride.setCreatedDate(LocalDate.now());

        assertEquals("ride123", ride.getId());
        assertEquals("user123", ride.getUserId());
        assertEquals("driver123", ride.getDriverId());
        assertEquals("Location A", ride.getPickupLocation());
        assertEquals("Location B", ride.getDropLocation());
        assertEquals("REQUESTED", ride.getStatus());
        assertEquals(100.0, ride.getFareAmount());
        assertEquals(10.0, ride.getDistanceKm());
        assertNotNull(ride.getCreatedAt());
        assertNotNull(ride.getCreatedDate());
    }

    @Test
    void testRideAllArgsConstructor() {
        LocalDateTime now = LocalDateTime.now();
        LocalDate today = LocalDate.now();
        
        Ride ride = new Ride("ride123", "user123", "driver123", "Location A", 
                            "Location B", "REQUESTED", now, today, 100.0, 10.0);

        assertEquals("ride123", ride.getId());
        assertEquals("user123", ride.getUserId());
        assertEquals("driver123", ride.getDriverId());
        assertEquals("REQUESTED", ride.getStatus());
        assertEquals(100.0, ride.getFareAmount());
        assertEquals(10.0, ride.getDistanceKm());
    }

    @Test
    void testRideNoArgsConstructor() {
        Ride ride = new Ride();
        
        assertNotNull(ride);
        assertNull(ride.getId());
        assertNull(ride.getStatus());
    }

    @Test
    void testRideStatusTransition() {
        Ride ride = new Ride();
        ride.setStatus("REQUESTED");
        assertEquals("REQUESTED", ride.getStatus());

        ride.setStatus("ACCEPTED");
        assertEquals("ACCEPTED", ride.getStatus());

        ride.setStatus("COMPLETED");
        assertEquals("COMPLETED", ride.getStatus());
    }

    @Test
    void testRideWithNullDriver() {
        Ride ride = new Ride();
        ride.setUserId("user123");
        ride.setDriverId(null);

        assertNull(ride.getDriverId());
        assertNotNull(ride.getUserId());
    }

    @Test
    void testRideCancelledStatus() {
        Ride ride = new Ride();
        ride.setStatus("CANCELLED");

        assertEquals("CANCELLED", ride.getStatus());
    }

    @Test
    void testRideFareAndDistance() {
        Ride ride = new Ride();
        ride.setFareAmount(250.75);
        ride.setDistanceKm(22.5);

        assertEquals(250.75, ride.getFareAmount());
        assertEquals(22.5, ride.getDistanceKm());
    }

    @Test
    void testRideEqualsAndHashCode() {
        LocalDateTime now = LocalDateTime.now();
        LocalDate today = LocalDate.now();
        
        Ride ride1 = new Ride("ride123", "user123", "driver123", "Location A", 
                             "Location B", "REQUESTED", now, today, 100.0, 10.0);
        Ride ride2 = new Ride("ride123", "user123", "driver123", "Location A", 
                             "Location B", "REQUESTED", now, today, 100.0, 10.0);

        assertEquals(ride1, ride2);
        assertEquals(ride1.hashCode(), ride2.hashCode());
    }
}
