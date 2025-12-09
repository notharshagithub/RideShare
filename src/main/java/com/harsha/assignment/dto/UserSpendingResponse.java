package com.harsha.assignment.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserSpendingResponse {
    private String userId;
    private Long totalCompletedRides;
    private Double totalSpent;
}
