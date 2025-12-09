package com.harsha.assignment.service;

import com.harsha.assignment.dto.CreateRideRequest;
import com.harsha.assignment.dto.RideResponse;
import com.harsha.assignment.exception.BadRequestException;
import com.harsha.assignment.exception.NotFoundException;
import com.harsha.assignment.model.Ride;
import com.harsha.assignment.model.User;
import com.harsha.assignment.repository.RideRepository;
import com.harsha.assignment.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RideServiceTest {

    @Mock
    private RideRepository rideRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private MongoTemplate mongoTemplate;

    @Mock
    private SecurityContext securityContext;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private RideService rideService;

    private User testUser;
    private User testDriver;
    private Ride testRide;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setId("user123");
        testUser.setUsername("testuser");
        testUser.setPassword("password");
        testUser.setRole("ROLE_USER");

        testDriver = new User();
        testDriver.setId("driver123");
        testDriver.setUsername("testdriver");
        testDriver.setPassword("password");
        testDriver.setRole("ROLE_DRIVER");

        testRide = new Ride();
        testRide.setId("ride123");
        testRide.setUserId("user123");
        testRide.setPickupLocation("Location A");
        testRide.setDropLocation("Location B");
        testRide.setFareAmount(100.0);
        testRide.setDistanceKm(10.0);
        testRide.setStatus("REQUESTED");
        testRide.setCreatedAt(LocalDateTime.now());
        testRide.setCreatedDate(LocalDate.now());

        // Setup security context
        SecurityContextHolder.setContext(securityContext);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn("testuser");
    }

    @Test
    void testCreateRide_Success() {
        CreateRideRequest request = new CreateRideRequest();
        request.setPickupLocation("Location A");
        request.setDropLocation("Location B");
        request.setFareAmount(100.0);
        request.setDistanceKm(10.0);

        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(testUser));
        when(rideRepository.save(any(Ride.class))).thenReturn(testRide);

        RideResponse response = rideService.createRide(request);

        assertNotNull(response);
        assertEquals("ride123", response.getId());
        assertEquals("Location A", response.getPickupLocation());
        assertEquals("Location B", response.getDropLocation());
        assertEquals(100.0, response.getFareAmount());
        assertEquals(10.0, response.getDistanceKm());
        assertEquals("REQUESTED", response.getStatus());
        
        verify(rideRepository).save(any(Ride.class));
    }

    @Test
    void testCreateRide_DriverCannotCreateRide() {
        CreateRideRequest request = new CreateRideRequest();
        request.setPickupLocation("Location A");
        request.setDropLocation("Location B");
        request.setFareAmount(100.0);
        request.setDistanceKm(10.0);

        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(testDriver));

        assertThrows(BadRequestException.class, () -> rideService.createRide(request));
        verify(rideRepository, never()).save(any(Ride.class));
    }

    @Test
    void testAcceptRide_Success() {
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(testDriver));
        when(rideRepository.findById("ride123")).thenReturn(Optional.of(testRide));
        when(rideRepository.save(any(Ride.class))).thenReturn(testRide);

        RideResponse response = rideService.acceptRide("ride123");

        assertNotNull(response);
        assertEquals("ACCEPTED", response.getStatus());
        assertEquals("driver123", response.getDriverId());
        
        verify(rideRepository).save(any(Ride.class));
    }

    @Test
    void testAcceptRide_OnlyDriverCanAccept() {
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(testUser));

        assertThrows(BadRequestException.class, () -> rideService.acceptRide("ride123"));
        verify(rideRepository, never()).save(any(Ride.class));
    }

    @Test
    void testAcceptRide_RideNotFound() {
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(testDriver));
        when(rideRepository.findById("ride123")).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> rideService.acceptRide("ride123"));
    }

    @Test
    void testCompleteRide_Success() {
        testRide.setStatus("ACCEPTED");
        testRide.setDriverId("driver123");

        when(rideRepository.findById("ride123")).thenReturn(Optional.of(testRide));
        when(rideRepository.save(any(Ride.class))).thenReturn(testRide);

        RideResponse response = rideService.completeRide("ride123");

        assertNotNull(response);
        assertEquals("COMPLETED", response.getStatus());
        
        verify(rideRepository).save(any(Ride.class));
    }

    @Test
    void testCompleteRide_RideNotFound() {
        when(rideRepository.findById("ride123")).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> rideService.completeRide("ride123"));
    }

    @Test
    void testGetUserRides_Success() {
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(testUser));
        when(rideRepository.findByUserId("user123")).thenReturn(Arrays.asList(testRide));

        List<RideResponse> rides = rideService.getUserRides();

        assertNotNull(rides);
        assertEquals(1, rides.size());
        assertEquals("ride123", rides.get(0).getId());
    }

    @Test
    void testGetPendingRides_Success() {
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(testDriver));
        when(rideRepository.findByStatus("REQUESTED")).thenReturn(Arrays.asList(testRide));

        List<RideResponse> rides = rideService.getPendingRides();

        assertNotNull(rides);
        assertEquals(1, rides.size());
        assertEquals("REQUESTED", rides.get(0).getStatus());
    }

    @Test
    void testSearchRides_Success() {
        when(mongoTemplate.find(any(Query.class), eq(Ride.class))).thenReturn(Arrays.asList(testRide));

        List<RideResponse> rides = rideService.searchRides("Location");

        assertNotNull(rides);
        assertEquals(1, rides.size());
    }

    @Test
    void testFilterByDistance_Success() {
        when(mongoTemplate.find(any(Query.class), eq(Ride.class))).thenReturn(Arrays.asList(testRide));

        List<RideResponse> rides = rideService.filterByDistance(5.0, 15.0);

        assertNotNull(rides);
        assertEquals(1, rides.size());
    }

    @Test
    void testFilterByDateRange_Success() {
        LocalDate start = LocalDate.now().minusDays(1);
        LocalDate end = LocalDate.now().plusDays(1);
        
        when(mongoTemplate.find(any(Query.class), eq(Ride.class))).thenReturn(Arrays.asList(testRide));

        List<RideResponse> rides = rideService.filterByDateRange(start, end);

        assertNotNull(rides);
        assertEquals(1, rides.size());
    }

    @Test
    void testSortByFare_Ascending() {
        when(mongoTemplate.find(any(Query.class), eq(Ride.class))).thenReturn(Arrays.asList(testRide));

        List<RideResponse> rides = rideService.sortByFare("asc");

        assertNotNull(rides);
        verify(mongoTemplate).find(any(Query.class), eq(Ride.class));
    }

    @Test
    void testSortByFare_Descending() {
        when(mongoTemplate.find(any(Query.class), eq(Ride.class))).thenReturn(Arrays.asList(testRide));

        List<RideResponse> rides = rideService.sortByFare("desc");

        assertNotNull(rides);
        verify(mongoTemplate).find(any(Query.class), eq(Ride.class));
    }

    @Test
    void testGetRidesByUserId_Success() {
        when(rideRepository.findByUserId("user123")).thenReturn(Arrays.asList(testRide));

        List<RideResponse> rides = rideService.getRidesByUserId("user123");

        assertNotNull(rides);
        assertEquals(1, rides.size());
    }

    @Test
    void testGetRidesByUserIdAndStatus_Success() {
        when(mongoTemplate.find(any(Query.class), eq(Ride.class))).thenReturn(Arrays.asList(testRide));

        List<RideResponse> rides = rideService.getRidesByUserIdAndStatus("user123", "REQUESTED");

        assertNotNull(rides);
        assertEquals(1, rides.size());
    }

    @Test
    void testGetActiveRidesForDriver_Success() {
        testRide.setStatus("ACCEPTED");
        testRide.setDriverId("driver123");
        
        when(mongoTemplate.find(any(Query.class), eq(Ride.class))).thenReturn(Arrays.asList(testRide));

        List<RideResponse> rides = rideService.getActiveRidesForDriver("driver123");

        assertNotNull(rides);
        assertEquals(1, rides.size());
    }

    @Test
    void testFilterByStatusAndKeyword_Success() {
        when(mongoTemplate.find(any(Query.class), eq(Ride.class))).thenReturn(Arrays.asList(testRide));

        List<RideResponse> rides = rideService.filterByStatusAndKeyword("REQUESTED", "Location");

        assertNotNull(rides);
        assertEquals(1, rides.size());
    }

    @Test
    void testAdvancedSearch_WithAllParameters() {
        when(mongoTemplate.find(any(Query.class), eq(Ride.class))).thenReturn(Arrays.asList(testRide));

        List<RideResponse> rides = rideService.advancedSearch("Location", "REQUESTED", "fareAmount", "asc", 0, 10);

        assertNotNull(rides);
        assertEquals(1, rides.size());
    }

    @Test
    void testAdvancedSearch_WithOnlySearch() {
        when(mongoTemplate.find(any(Query.class), eq(Ride.class))).thenReturn(Arrays.asList(testRide));

        List<RideResponse> rides = rideService.advancedSearch("Location", null, "fareAmount", "asc", 0, 10);

        assertNotNull(rides);
        verify(mongoTemplate).find(any(Query.class), eq(Ride.class));
    }

    @Test
    void testAdvancedSearch_WithOnlyStatus() {
        when(mongoTemplate.find(any(Query.class), eq(Ride.class))).thenReturn(Arrays.asList(testRide));

        List<RideResponse> rides = rideService.advancedSearch(null, "REQUESTED", "fareAmount", "asc", 0, 10);

        assertNotNull(rides);
        verify(mongoTemplate).find(any(Query.class), eq(Ride.class));
    }

    @Test
    void testGetRidesByDate_Success() {
        LocalDate date = LocalDate.now();
        
        when(mongoTemplate.find(any(Query.class), eq(Ride.class))).thenReturn(Arrays.asList(testRide));

        List<RideResponse> rides = rideService.getRidesByDate(date);

        assertNotNull(rides);
        assertEquals(1, rides.size());
    }
}
