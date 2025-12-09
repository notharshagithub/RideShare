package com.harsha.assignment.controller;

import com.harsha.assignment.dto.DriverSummaryResponse;
import com.harsha.assignment.dto.RidesPerDayResponse;
import com.harsha.assignment.dto.StatusSummaryResponse;
import com.harsha.assignment.dto.UserSpendingResponse;
import com.harsha.assignment.service.AnalyticsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AnalyticsController.class)
@AutoConfigureMockMvc(addFilters = false)
class AnalyticsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AnalyticsService analyticsService;

    private List<RidesPerDayResponse> ridesPerDayList;
    private DriverSummaryResponse driverSummary;
    private UserSpendingResponse userSpending;
    private List<StatusSummaryResponse> statusSummaryList;

    @BeforeEach
    void setUp() {
        ridesPerDayList = Arrays.asList(
            new RidesPerDayResponse(LocalDate.now(), 10L),
            new RidesPerDayResponse(LocalDate.now().minusDays(1), 8L)
        );

        driverSummary = new DriverSummaryResponse(
            "driver123", 100L, 90L, 10L, 15.5, 25000.0
        );

        userSpending = new UserSpendingResponse("user123", 50L, 12500.0);

        statusSummaryList = Arrays.asList(
            new StatusSummaryResponse("COMPLETED", 100L),
            new StatusSummaryResponse("REQUESTED", 45L),
            new StatusSummaryResponse("ACCEPTED", 30L)
        );
    }

    @Test
    @WithMockUser
    void testGetRidesPerDay_Success() throws Exception {
        when(analyticsService.getRidesPerDay()).thenReturn(ridesPerDayList);

        mockMvc.perform(get("/api/v1/analytics/rides-per-day"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].count").value(10))
                .andExpect(jsonPath("$[1].count").value(8));

        verify(analyticsService).getRidesPerDay();
    }

    @Test
    @WithMockUser
    void testGetRidesPerDay_EmptyResults() throws Exception {
        when(analyticsService.getRidesPerDay()).thenReturn(Arrays.asList());

        mockMvc.perform(get("/api/v1/analytics/rides-per-day"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isEmpty());

        verify(analyticsService).getRidesPerDay();
    }

    @Test
    @WithMockUser
    void testGetDriverSummary_Success() throws Exception {
        when(analyticsService.getDriverSummary("driver123")).thenReturn(driverSummary);

        mockMvc.perform(get("/api/v1/analytics/driver/driver123/summary"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.driverId").value("driver123"))
                .andExpect(jsonPath("$.totalRides").value(100))
                .andExpect(jsonPath("$.completedRides").value(90))
                .andExpect(jsonPath("$.cancelledRides").value(10))
                .andExpect(jsonPath("$.avgDistance").value(15.5))
                .andExpect(jsonPath("$.totalFare").value(25000.0));

        verify(analyticsService).getDriverSummary("driver123");
    }

    @Test
    @WithMockUser
    void testGetDriverSummary_NoData() throws Exception {
        DriverSummaryResponse emptyResponse = new DriverSummaryResponse(
            "driver999", 0L, 0L, 0L, 0.0, 0.0
        );
        when(analyticsService.getDriverSummary("driver999")).thenReturn(emptyResponse);

        mockMvc.perform(get("/api/v1/analytics/driver/driver999/summary"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalRides").value(0));

        verify(analyticsService).getDriverSummary("driver999");
    }

    @Test
    @WithMockUser
    void testGetUserSpending_Success() throws Exception {
        when(analyticsService.getUserSpending("user123")).thenReturn(userSpending);

        mockMvc.perform(get("/api/v1/analytics/user/user123/spending"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value("user123"))
                .andExpect(jsonPath("$.totalCompletedRides").value(50))
                .andExpect(jsonPath("$.totalSpent").value(12500.0));

        verify(analyticsService).getUserSpending("user123");
    }

    @Test
    @WithMockUser
    void testGetUserSpending_NoData() throws Exception {
        UserSpendingResponse emptyResponse = new UserSpendingResponse("user999", 0L, 0.0);
        when(analyticsService.getUserSpending("user999")).thenReturn(emptyResponse);

        mockMvc.perform(get("/api/v1/analytics/user/user999/spending"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalSpent").value(0.0));

        verify(analyticsService).getUserSpending("user999");
    }

    @Test
    @WithMockUser
    void testGetStatusSummary_Success() throws Exception {
        when(analyticsService.getStatusSummary()).thenReturn(statusSummaryList);

        mockMvc.perform(get("/api/v1/analytics/status-summary"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].status").value("COMPLETED"))
                .andExpect(jsonPath("$[0].count").value(100))
                .andExpect(jsonPath("$[1].status").value("REQUESTED"))
                .andExpect(jsonPath("$[1].count").value(45))
                .andExpect(jsonPath("$[2].status").value("ACCEPTED"))
                .andExpect(jsonPath("$[2].count").value(30));

        verify(analyticsService).getStatusSummary();
    }

    @Test
    @WithMockUser
    void testGetStatusSummary_EmptyResults() throws Exception {
        when(analyticsService.getStatusSummary()).thenReturn(Arrays.asList());

        mockMvc.perform(get("/api/v1/analytics/status-summary"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isEmpty());

        verify(analyticsService).getStatusSummary();
    }
}
