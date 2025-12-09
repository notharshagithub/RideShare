package com.harsha.assignment.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.harsha.assignment.dto.CreateRideRequest;
import com.harsha.assignment.dto.RideResponse;
import com.harsha.assignment.service.RideService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(RideController.class)
@AutoConfigureMockMvc(addFilters = false)
class RideControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private RideService rideService;

    private RideResponse testRideResponse;
    private CreateRideRequest createRideRequest;

    @BeforeEach
    void setUp() {
        testRideResponse = new RideResponse();
        testRideResponse.setId("ride123");
        testRideResponse.setUserId("user123");
        testRideResponse.setPickupLocation("Location A");
        testRideResponse.setDropLocation("Location B");
        testRideResponse.setFareAmount(100.0);
        testRideResponse.setDistanceKm(10.0);
        testRideResponse.setStatus("REQUESTED");
        testRideResponse.setCreatedAt(LocalDateTime.now());
        testRideResponse.setCreatedDate(LocalDate.now());

        createRideRequest = new CreateRideRequest();
        createRideRequest.setPickupLocation("Location A");
        createRideRequest.setDropLocation("Location B");
        createRideRequest.setFareAmount(100.0);
        createRideRequest.setDistanceKm(10.0);
    }

    @Test
    @WithMockUser
    void testCreateRide_Success() throws Exception {
        when(rideService.createRide(any(CreateRideRequest.class))).thenReturn(testRideResponse);

        mockMvc.perform(post("/api/v1/rides")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createRideRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("ride123"))
                .andExpect(jsonPath("$.pickupLocation").value("Location A"))
                .andExpect(jsonPath("$.dropLocation").value("Location B"))
                .andExpect(jsonPath("$.fareAmount").value(100.0))
                .andExpect(jsonPath("$.distanceKm").value(10.0))
                .andExpect(jsonPath("$.status").value("REQUESTED"));

        verify(rideService).createRide(any(CreateRideRequest.class));
    }

    @Test
    @WithMockUser
    void testCompleteRide_Success() throws Exception {
        testRideResponse.setStatus("COMPLETED");
        when(rideService.completeRide("ride123")).thenReturn(testRideResponse);

        mockMvc.perform(post("/api/v1/rides/ride123/complete"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("COMPLETED"));

        verify(rideService).completeRide("ride123");
    }

    @Test
    @WithMockUser
    void testGetUserRides_Success() throws Exception {
        List<RideResponse> rides = Arrays.asList(testRideResponse);
        when(rideService.getUserRides()).thenReturn(rides);

        mockMvc.perform(get("/api/v1/user/rides"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value("ride123"));

        verify(rideService).getUserRides();
    }

    @Test
    @WithMockUser(roles = "DRIVER")
    void testGetPendingRides_Success() throws Exception {
        List<RideResponse> rides = Arrays.asList(testRideResponse);
        when(rideService.getPendingRides()).thenReturn(rides);

        mockMvc.perform(get("/api/v1/driver/rides/requests"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].status").value("REQUESTED"));

        verify(rideService).getPendingRides();
    }

    @Test
    @WithMockUser(roles = "DRIVER")
    void testAcceptRide_Success() throws Exception {
        testRideResponse.setStatus("ACCEPTED");
        when(rideService.acceptRide("ride123")).thenReturn(testRideResponse);

        mockMvc.perform(post("/api/v1/driver/rides/ride123/accept"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("ACCEPTED"));

        verify(rideService).acceptRide("ride123");
    }

    @Test
    @WithMockUser(roles = "DRIVER")
    void testGetDriverRides_Success() throws Exception {
        List<RideResponse> rides = Arrays.asList(testRideResponse);
        when(rideService.getDriverRides()).thenReturn(rides);

        mockMvc.perform(get("/api/v1/driver/rides"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value("ride123"));

        verify(rideService).getDriverRides();
    }

    @Test
    @WithMockUser
    void testSearchRides_Success() throws Exception {
        List<RideResponse> rides = Arrays.asList(testRideResponse);
        when(rideService.searchRides("Location")).thenReturn(rides);

        mockMvc.perform(get("/api/v1/rides/search")
                .param("text", "Location"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].pickupLocation").value("Location A"));

        verify(rideService).searchRides("Location");
    }

    @Test
    @WithMockUser
    void testFilterByDistance_Success() throws Exception {
        List<RideResponse> rides = Arrays.asList(testRideResponse);
        when(rideService.filterByDistance(5.0, 15.0)).thenReturn(rides);

        mockMvc.perform(get("/api/v1/rides/filter-distance")
                .param("min", "5.0")
                .param("max", "15.0"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].distanceKm").value(10.0));

        verify(rideService).filterByDistance(5.0, 15.0);
    }

    @Test
    @WithMockUser
    void testFilterByDateRange_Success() throws Exception {
        List<RideResponse> rides = Arrays.asList(testRideResponse);
        LocalDate start = LocalDate.now().minusDays(1);
        LocalDate end = LocalDate.now().plusDays(1);
        
        when(rideService.filterByDateRange(any(LocalDate.class), any(LocalDate.class)))
            .thenReturn(rides);

        mockMvc.perform(get("/api/v1/rides/filter-date-range")
                .param("start", start.toString())
                .param("end", end.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value("ride123"));

        verify(rideService).filterByDateRange(any(LocalDate.class), any(LocalDate.class));
    }

    @Test
    @WithMockUser
    void testSortByFare_Success() throws Exception {
        List<RideResponse> rides = Arrays.asList(testRideResponse);
        when(rideService.sortByFare("desc")).thenReturn(rides);

        mockMvc.perform(get("/api/v1/rides/sort")
                .param("order", "desc"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].fareAmount").value(100.0));

        verify(rideService).sortByFare("desc");
    }

    @Test
    @WithMockUser
    void testGetRidesByUserId_Success() throws Exception {
        List<RideResponse> rides = Arrays.asList(testRideResponse);
        when(rideService.getRidesByUserId("user123")).thenReturn(rides);

        mockMvc.perform(get("/api/v1/rides/user/user123"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].userId").value("user123"));

        verify(rideService).getRidesByUserId("user123");
    }

    @Test
    @WithMockUser
    void testGetRidesByUserIdAndStatus_Success() throws Exception {
        List<RideResponse> rides = Arrays.asList(testRideResponse);
        when(rideService.getRidesByUserIdAndStatus("user123", "REQUESTED")).thenReturn(rides);

        mockMvc.perform(get("/api/v1/rides/user/user123/status/REQUESTED"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].status").value("REQUESTED"));

        verify(rideService).getRidesByUserIdAndStatus("user123", "REQUESTED");
    }

    @Test
    @WithMockUser
    void testGetActiveRidesForDriver_Success() throws Exception {
        testRideResponse.setStatus("ACCEPTED");
        List<RideResponse> rides = Arrays.asList(testRideResponse);
        when(rideService.getActiveRidesForDriver("driver123")).thenReturn(rides);

        mockMvc.perform(get("/api/v1/driver/driver123/active-rides"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].status").value("ACCEPTED"));

        verify(rideService).getActiveRidesForDriver("driver123");
    }

    @Test
    @WithMockUser
    void testFilterByStatusAndKeyword_Success() throws Exception {
        List<RideResponse> rides = Arrays.asList(testRideResponse);
        when(rideService.filterByStatusAndKeyword("REQUESTED", "Location")).thenReturn(rides);

        mockMvc.perform(get("/api/v1/rides/filter-status")
                .param("status", "REQUESTED")
                .param("search", "Location"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].status").value("REQUESTED"));

        verify(rideService).filterByStatusAndKeyword("REQUESTED", "Location");
    }

    @Test
    @WithMockUser
    void testAdvancedSearch_Success() throws Exception {
        List<RideResponse> rides = Arrays.asList(testRideResponse);
        when(rideService.advancedSearch(anyString(), anyString(), anyString(), anyString(), anyInt(), anyInt()))
            .thenReturn(rides);

        mockMvc.perform(get("/api/v1/rides/advanced-search")
                .param("search", "Location")
                .param("status", "REQUESTED")
                .param("sort", "fareAmount")
                .param("order", "asc")
                .param("page", "0")
                .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value("ride123"));

        verify(rideService).advancedSearch("Location", "REQUESTED", "fareAmount", "asc", 0, 10);
    }

    @Test
    @WithMockUser
    void testGetRidesByDate_Success() throws Exception {
        List<RideResponse> rides = Arrays.asList(testRideResponse);
        when(rideService.getRidesByDate(any(LocalDate.class))).thenReturn(rides);

        mockMvc.perform(get("/api/v1/rides/date/" + LocalDate.now()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value("ride123"));

        verify(rideService).getRidesByDate(any(LocalDate.class));
    }

    @Test
    @WithMockUser
    void testCreateRide_InvalidRequest_MissingPickup() throws Exception {
        createRideRequest.setPickupLocation(null);

        mockMvc.perform(post("/api/v1/rides")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createRideRequest)))
                .andExpect(status().isBadRequest());

        verify(rideService, never()).createRide(any(CreateRideRequest.class));
    }

    @Test
    @WithMockUser
    void testCreateRide_InvalidRequest_NegativeFare() throws Exception {
        createRideRequest.setFareAmount(-10.0);

        mockMvc.perform(post("/api/v1/rides")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createRideRequest)))
                .andExpect(status().isBadRequest());

        verify(rideService, never()).createRide(any(CreateRideRequest.class));
    }
}
