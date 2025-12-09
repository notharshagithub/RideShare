package com.harsha.assignment.service;

import com.harsha.assignment.dto.DriverSummaryResponse;
import com.harsha.assignment.dto.RidesPerDayResponse;
import com.harsha.assignment.dto.StatusSummaryResponse;
import com.harsha.assignment.dto.UserSpendingResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.GroupOperation;
import org.springframework.data.mongodb.core.aggregation.MatchOperation;
import org.springframework.data.mongodb.core.aggregation.ProjectionOperation;
import org.springframework.data.mongodb.core.aggregation.SortOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;

import java.util.List;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.*;

@Service
public class AnalyticsService {

    @Autowired
    private MongoTemplate mongoTemplate;

    // API 10: Rides per day
    public List<RidesPerDayResponse> getRidesPerDay() {
        GroupOperation groupByDate = group("createdDate")
                .count().as("count");
        
        ProjectionOperation project = project()
                .and("_id").as("date")
                .andInclude("count");
        
        SortOperation sort = sort(Sort.Direction.DESC, "date");
        
        Aggregation aggregation = newAggregation(
                groupByDate,
                project,
                sort
        );
        
        AggregationResults<RidesPerDayResponse> results = mongoTemplate.aggregate(
                aggregation, "rides", RidesPerDayResponse.class
        );
        
        return results.getMappedResults();
    }

    // API 11: Driver summary
    public DriverSummaryResponse getDriverSummary(String driverId) {
        MatchOperation matchDriver = match(Criteria.where("driverId").is(driverId));
        
        GroupOperation group = group()
                .count().as("totalRides")
                .sum(Aggregation.ConditionalOperators.when(Criteria.where("status").is("COMPLETED"))
                        .then(1).otherwise(0)).as("completedRides")
                .sum(Aggregation.ConditionalOperators.when(Criteria.where("status").is("CANCELLED"))
                        .then(1).otherwise(0)).as("cancelledRides")
                .avg("distanceKm").as("avgDistance")
                .sum("fareAmount").as("totalFare");
        
        ProjectionOperation project = project()
                .andExpression("'" + driverId + "'").as("driverId")
                .andInclude("totalRides", "completedRides", "cancelledRides", "avgDistance", "totalFare");
        
        Aggregation aggregation = newAggregation(
                matchDriver,
                group,
                project
        );
        
        AggregationResults<DriverSummaryResponse> results = mongoTemplate.aggregate(
                aggregation, "rides", DriverSummaryResponse.class
        );
        
        DriverSummaryResponse result = results.getUniqueMappedResult();
        if (result == null) {
            result = new DriverSummaryResponse(driverId, 0L, 0L, 0L, 0.0, 0.0);
        }
        return result;
    }

    // API 12: User spending
    public UserSpendingResponse getUserSpending(String userId) {
        MatchOperation matchUser = match(Criteria.where("userId").is(userId)
                .and("status").is("COMPLETED"));
        
        GroupOperation group = group()
                .count().as("totalCompletedRides")
                .sum("fareAmount").as("totalSpent");
        
        ProjectionOperation project = project()
                .andExpression("'" + userId + "'").as("userId")
                .andInclude("totalCompletedRides", "totalSpent");
        
        Aggregation aggregation = newAggregation(
                matchUser,
                group,
                project
        );
        
        AggregationResults<UserSpendingResponse> results = mongoTemplate.aggregate(
                aggregation, "rides", UserSpendingResponse.class
        );
        
        UserSpendingResponse result = results.getUniqueMappedResult();
        if (result == null) {
            result = new UserSpendingResponse(userId, 0L, 0.0);
        }
        return result;
    }

    // API 13: Status summary
    public List<StatusSummaryResponse> getStatusSummary() {
        GroupOperation groupByStatus = group("status")
                .count().as("count");
        
        ProjectionOperation project = project()
                .and("_id").as("status")
                .andInclude("count");
        
        SortOperation sort = sort(Sort.Direction.DESC, "count");
        
        Aggregation aggregation = newAggregation(
                groupByStatus,
                project,
                sort
        );
        
        AggregationResults<StatusSummaryResponse> results = mongoTemplate.aggregate(
                aggregation, "rides", StatusSummaryResponse.class
        );
        
        return results.getMappedResults();
    }
}
