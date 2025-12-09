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
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RideService {
    
    @Autowired
    private RideRepository rideRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private MongoTemplate mongoTemplate;
    
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
        ride.setFareAmount(request.getFareAmount());
        ride.setDistanceKm(request.getDistanceKm());
        ride.setStatus("REQUESTED");
        LocalDateTime now = LocalDateTime.now();
        ride.setCreatedAt(now);
        ride.setCreatedDate(now.toLocalDate());
        
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
    
    // API 1: Search rides by pickup OR drop location (keyword search with regex)
    public List<RideResponse> searchRides(String text) {
        Criteria criteria = new Criteria().orOperator(
                Criteria.where("pickupLocation").regex(text, "i"),
                Criteria.where("dropLocation").regex(text, "i")
        );
        
        Query query = new Query(criteria);
        List<Ride> rides = mongoTemplate.find(query, Ride.class);
        
        return rides.stream()
                .map(RideResponse::fromRide)
                .collect(Collectors.toList());
    }
    
    // API 2: Filter rides by distance range
    public List<RideResponse> filterByDistance(Double min, Double max) {
        Criteria criteria = Criteria.where("distanceKm").gte(min).lte(max);
        Query query = new Query(criteria);
        List<Ride> rides = mongoTemplate.find(query, Ride.class);
        
        return rides.stream()
                .map(RideResponse::fromRide)
                .collect(Collectors.toList());
    }
    
    // API 3: Filter rides by date range
    public List<RideResponse> filterByDateRange(LocalDate start, LocalDate end) {
        Criteria criteria = Criteria.where("createdDate").gte(start).lte(end);
        Query query = new Query(criteria);
        List<Ride> rides = mongoTemplate.find(query, Ride.class);
        
        return rides.stream()
                .map(RideResponse::fromRide)
                .collect(Collectors.toList());
    }
    
    // API 4: Sort rides by fare
    public List<RideResponse> sortByFare(String order) {
        Sort.Direction direction = order.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
        Query query = new Query();
        query.with(Sort.by(direction, "fareAmount"));
        
        List<Ride> rides = mongoTemplate.find(query, Ride.class);
        
        return rides.stream()
                .map(RideResponse::fromRide)
                .collect(Collectors.toList());
    }
    
    // API 5: Get rides for a specific user (already exists as getUserRides but adding by userId parameter)
    public List<RideResponse> getRidesByUserId(String userId) {
        List<Ride> rides = rideRepository.findByUserId(userId);
        return rides.stream()
                .map(RideResponse::fromRide)
                .collect(Collectors.toList());
    }
    
    // API 6: Get rides for user filtered by status
    public List<RideResponse> getRidesByUserIdAndStatus(String userId, String status) {
        Criteria criteria = Criteria.where("userId").is(userId).and("status").is(status);
        Query query = new Query(criteria);
        List<Ride> rides = mongoTemplate.find(query, Ride.class);
        
        return rides.stream()
                .map(RideResponse::fromRide)
                .collect(Collectors.toList());
    }
    
    // API 7: Get active rides for a driver
    public List<RideResponse> getActiveRidesForDriver(String driverId) {
        Criteria criteria = Criteria.where("driverId").is(driverId).and("status").is("ACCEPTED");
        Query query = new Query(criteria);
        List<Ride> rides = mongoTemplate.find(query, Ride.class);
        
        return rides.stream()
                .map(RideResponse::fromRide)
                .collect(Collectors.toList());
    }
    
    // API 8: Filter rides by status + keyword search
    public List<RideResponse> filterByStatusAndKeyword(String status, String search) {
        Criteria statusCriteria = Criteria.where("status").is(status);
        Criteria searchCriteria = new Criteria().orOperator(
                Criteria.where("pickupLocation").regex(search, "i"),
                Criteria.where("dropLocation").regex(search, "i")
        );
        
        Criteria combinedCriteria = new Criteria().andOperator(statusCriteria, searchCriteria);
        Query query = new Query(combinedCriteria);
        List<Ride> rides = mongoTemplate.find(query, Ride.class);
        
        return rides.stream()
                .map(RideResponse::fromRide)
                .collect(Collectors.toList());
    }
    
    // API 9: Advanced search with keyword, status, sort, and pagination
    public List<RideResponse> advancedSearch(String search, String status, String sortBy, String order, int page, int size) {
        Query query = new Query();
        
        // Build criteria based on provided parameters
        boolean hasSearch = search != null && !search.isEmpty();
        boolean hasStatus = status != null && !status.isEmpty();
        
        if (hasSearch && hasStatus) {
            // Both search and status provided
            Criteria statusCriteria = Criteria.where("status").is(status);
            Criteria searchCriteria = new Criteria().orOperator(
                    Criteria.where("pickupLocation").regex(search, "i"),
                    Criteria.where("dropLocation").regex(search, "i")
            );
            Criteria combinedCriteria = new Criteria().andOperator(statusCriteria, searchCriteria);
            query.addCriteria(combinedCriteria);
        } else if (hasSearch) {
            // Only search provided
            Criteria searchCriteria = new Criteria().orOperator(
                    Criteria.where("pickupLocation").regex(search, "i"),
                    Criteria.where("dropLocation").regex(search, "i")
            );
            query.addCriteria(searchCriteria);
        } else if (hasStatus) {
            // Only status provided
            query.addCriteria(Criteria.where("status").is(status));
        }
        // If neither provided, query will return all rides
        
        // Add sorting
        if (sortBy != null && !sortBy.isEmpty()) {
            Sort.Direction direction = "desc".equalsIgnoreCase(order) ? Sort.Direction.DESC : Sort.Direction.ASC;
            query.with(Sort.by(direction, sortBy));
        }
        
        // Add pagination
        Pageable pageable = PageRequest.of(page, size);
        query.with(pageable);
        
        List<Ride> rides = mongoTemplate.find(query, Ride.class);
        
        return rides.stream()
                .map(RideResponse::fromRide)
                .collect(Collectors.toList());
    }
    
    // API 14: Get rides on a specific date
    public List<RideResponse> getRidesByDate(LocalDate date) {
        Criteria criteria = Criteria.where("createdDate").is(date);
        Query query = new Query(criteria);
        List<Ride> rides = mongoTemplate.find(query, Ride.class);
        
        return rides.stream()
                .map(RideResponse::fromRide)
                .collect(Collectors.toList());
    }
}
