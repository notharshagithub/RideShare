package com.harsha.assignment.service;

import com.harsha.assignment.dto.CreateRideRequest;
import com.harsha.assignment.dto.RideResponse;
import com.harsha.assignment.exception.BadRequestException;
import com.harsha.assignment.exception.NotFoundException;
import com.harsha.assignment.model.Ride;
import com.harsha.assignment.model.User;
import com.harsha.assignment.repository.RideRepository;
import com.harsha.assignment.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RideService {
    
    @Autowired
    private RideRepository rideRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    private String getCurrentUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getName();
    }
    
    private User getCurrentUser() {
        String username = getCurrentUsername();
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new NotFoundException("User not found"));
    }
    
    public RideResponse createRide(CreateRideRequest request) {
        User user = getCurrentUser();
        
        // Only ROLE_USER can create rides
        if (!user.getRole().equals("ROLE_USER")) {
            throw new BadRequestException("Only passengers can request rides");
        }
        
        Ride ride = new Ride();
        ride.setUserId(user.getId());
        ride.setPickupLocation(request.getPickupLocation());
        ride.setDropLocation(request.getDropLocation());
        ride.setStatus("REQUESTED");
        ride.setCreatedAt(LocalDateTime.now());
        
        Ride savedRide = rideRepository.save(ride);
        return RideResponse.fromRide(savedRide);
    }
    
    public List<RideResponse> getPendingRides() {
        List<Ride> rides = rideRepository.findByStatus("REQUESTED");
        return rides.stream()
                .map(RideResponse::fromRide)
                .collect(Collectors.toList());
    }
    
    public RideResponse acceptRide(String rideId) {
        User driver = getCurrentUser();
        
        // Only ROLE_DRIVER can accept rides
        if (!driver.getRole().equals("ROLE_DRIVER")) {
            throw new BadRequestException("Only drivers can accept rides");
        }
        
        Ride ride = rideRepository.findById(rideId)
                .orElseThrow(() -> new NotFoundException("Ride not found"));
        
        // Check if ride is in REQUESTED status
        if (!ride.getStatus().equals("REQUESTED")) {
            throw new BadRequestException("Ride is not available for acceptance");
        }
        
        ride.setDriverId(driver.getId());
        ride.setStatus("ACCEPTED");
        
        Ride updatedRide = rideRepository.save(ride);
        return RideResponse.fromRide(updatedRide);
    }
    
    public RideResponse completeRide(String rideId) {
        User currentUser = getCurrentUser();
        
        Ride ride = rideRepository.findById(rideId)
                .orElseThrow(() -> new NotFoundException("Ride not found"));
        
        // Check if ride is in ACCEPTED status
        if (!ride.getStatus().equals("ACCEPTED")) {
            throw new BadRequestException("Ride must be accepted before completion");
        }
        
        // Verify that the current user is either the passenger or the driver
        if (!ride.getUserId().equals(currentUser.getId()) && !currentUser.getId().equals(ride.getDriverId())) {
            throw new BadRequestException("You are not authorized to complete this ride");
        }
        
        ride.setStatus("COMPLETED");
        
        Ride updatedRide = rideRepository.save(ride);
        return RideResponse.fromRide(updatedRide);
    }
    
    public List<RideResponse> getUserRides() {
        User user = getCurrentUser();
        
        List<Ride> rides = rideRepository.findByUserId(user.getId());
        return rides.stream()
                .map(RideResponse::fromRide)
                .collect(Collectors.toList());
    }
    
    public List<RideResponse> getDriverRides() {
        User driver = getCurrentUser();
        
        List<Ride> rides = rideRepository.findByDriverId(driver.getId());
        return rides.stream()
                .map(RideResponse::fromRide)
                .collect(Collectors.toList());
    }
}
