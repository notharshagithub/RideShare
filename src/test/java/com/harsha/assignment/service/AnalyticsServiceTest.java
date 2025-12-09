package com.harsha.assignment.service;

import com.harsha.assignment.dto.DriverSummaryResponse;
import com.harsha.assignment.dto.RidesPerDayResponse;
import com.harsha.assignment.dto.StatusSummaryResponse;
import com.harsha.assignment.dto.UserSpendingResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AnalyticsServiceTest {

    @Mock
    private MongoTemplate mongoTemplate;

    @Mock
    private AggregationResults<RidesPerDayResponse> ridesPerDayResults;

    @Mock
    private AggregationResults<DriverSummaryResponse> driverSummaryResults;

    @Mock
    private AggregationResults<UserSpendingResponse> userSpendingResults;

    @Mock
    private AggregationResults<StatusSummaryResponse> statusSummaryResults;

    @InjectMocks
    private AnalyticsService analyticsService;

    @BeforeEach
    void setUp() {
    }

    @Test
    void testGetRidesPerDay_Success() {
        RidesPerDayResponse response1 = new RidesPerDayResponse(LocalDate.now(), 10L);
        RidesPerDayResponse response2 = new RidesPerDayResponse(LocalDate.now().minusDays(1), 8L);
        List<RidesPerDayResponse> expectedResults = Arrays.asList(response1, response2);

        when(mongoTemplate.aggregate(any(Aggregation.class), eq("rides"), eq(RidesPerDayResponse.class)))
            .thenReturn(ridesPerDayResults);
        when(ridesPerDayResults.getMappedResults()).thenReturn(expectedResults);

        List<RidesPerDayResponse> results = analyticsService.getRidesPerDay();

        assertNotNull(results);
        assertEquals(2, results.size());
        assertEquals(10L, results.get(0).getCount());
        verify(mongoTemplate).aggregate(any(Aggregation.class), eq("rides"), eq(RidesPerDayResponse.class));
    }

    @Test
    void testGetRidesPerDay_EmptyResults() {
        when(mongoTemplate.aggregate(any(Aggregation.class), eq("rides"), eq(RidesPerDayResponse.class)))
            .thenReturn(ridesPerDayResults);
        when(ridesPerDayResults.getMappedResults()).thenReturn(Arrays.asList());

        List<RidesPerDayResponse> results = analyticsService.getRidesPerDay();

        assertNotNull(results);
        assertTrue(results.isEmpty());
    }

    @Test
    void testGetDriverSummary_Success() {
        DriverSummaryResponse expectedResponse = new DriverSummaryResponse(
            "driver123", 100L, 90L, 10L, 15.5, 25000.0
        );

        when(mongoTemplate.aggregate(any(Aggregation.class), eq("rides"), eq(DriverSummaryResponse.class)))
            .thenReturn(driverSummaryResults);
        when(driverSummaryResults.getUniqueMappedResult()).thenReturn(expectedResponse);

        DriverSummaryResponse result = analyticsService.getDriverSummary("driver123");

        assertNotNull(result);
        assertEquals("driver123", result.getDriverId());
        assertEquals(100L, result.getTotalRides());
        assertEquals(90L, result.getCompletedRides());
        assertEquals(10L, result.getCancelledRides());
        assertEquals(15.5, result.getAvgDistance());
        assertEquals(25000.0, result.getTotalFare());
    }

    @Test
    void testGetDriverSummary_NoData() {
        when(mongoTemplate.aggregate(any(Aggregation.class), eq("rides"), eq(DriverSummaryResponse.class)))
            .thenReturn(driverSummaryResults);
        when(driverSummaryResults.getUniqueMappedResult()).thenReturn(null);

        DriverSummaryResponse result = analyticsService.getDriverSummary("driver123");

        assertNotNull(result);
        assertEquals("driver123", result.getDriverId());
        assertEquals(0L, result.getTotalRides());
        assertEquals(0L, result.getCompletedRides());
        assertEquals(0L, result.getCancelledRides());
        assertEquals(0.0, result.getAvgDistance());
        assertEquals(0.0, result.getTotalFare());
    }

    @Test
    void testGetUserSpending_Success() {
        UserSpendingResponse expectedResponse = new UserSpendingResponse("user123", 50L, 12500.0);

        when(mongoTemplate.aggregate(any(Aggregation.class), eq("rides"), eq(UserSpendingResponse.class)))
            .thenReturn(userSpendingResults);
        when(userSpendingResults.getUniqueMappedResult()).thenReturn(expectedResponse);

        UserSpendingResponse result = analyticsService.getUserSpending("user123");

        assertNotNull(result);
        assertEquals("user123", result.getUserId());
        assertEquals(50L, result.getTotalCompletedRides());
        assertEquals(12500.0, result.getTotalSpent());
    }

    @Test
    void testGetUserSpending_NoData() {
        when(mongoTemplate.aggregate(any(Aggregation.class), eq("rides"), eq(UserSpendingResponse.class)))
            .thenReturn(userSpendingResults);
        when(userSpendingResults.getUniqueMappedResult()).thenReturn(null);

        UserSpendingResponse result = analyticsService.getUserSpending("user123");

        assertNotNull(result);
        assertEquals("user123", result.getUserId());
        assertEquals(0L, result.getTotalCompletedRides());
        assertEquals(0.0, result.getTotalSpent());
    }

    @Test
    void testGetStatusSummary_Success() {
        StatusSummaryResponse response1 = new StatusSummaryResponse("COMPLETED", 100L);
        StatusSummaryResponse response2 = new StatusSummaryResponse("REQUESTED", 45L);
        StatusSummaryResponse response3 = new StatusSummaryResponse("ACCEPTED", 30L);
        List<StatusSummaryResponse> expectedResults = Arrays.asList(response1, response2, response3);

        when(mongoTemplate.aggregate(any(Aggregation.class), eq("rides"), eq(StatusSummaryResponse.class)))
            .thenReturn(statusSummaryResults);
        when(statusSummaryResults.getMappedResults()).thenReturn(expectedResults);

        List<StatusSummaryResponse> results = analyticsService.getStatusSummary();

        assertNotNull(results);
        assertEquals(3, results.size());
        assertEquals("COMPLETED", results.get(0).getStatus());
        assertEquals(100L, results.get(0).getCount());
        verify(mongoTemplate).aggregate(any(Aggregation.class), eq("rides"), eq(StatusSummaryResponse.class));
    }

    @Test
    void testGetStatusSummary_EmptyResults() {
        when(mongoTemplate.aggregate(any(Aggregation.class), eq("rides"), eq(StatusSummaryResponse.class)))
            .thenReturn(statusSummaryResults);
        when(statusSummaryResults.getMappedResults()).thenReturn(Arrays.asList());

        List<StatusSummaryResponse> results = analyticsService.getStatusSummary();

        assertNotNull(results);
        assertTrue(results.isEmpty());
    }
}
