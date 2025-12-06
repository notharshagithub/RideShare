package com.harsha.assignment.repository;

import com.harsha.assignment.model.Ride;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RideRepository extends MongoRepository<Ride, String> {
    List<Ride> findByStatus(String status);
    List<Ride> findByUserId(String userId);
    List<Ride> findByDriverId(String driverId);
}
