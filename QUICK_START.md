# 🚀 Quick Start Guide - RideShare Backend

## Prerequisites Check
- ✅ Java 21 installed
- ✅ MongoDB running on localhost:27017
- ✅ Port 8081 available

## Step-by-Step Setup

### 1. Start MongoDB
```bash
# Make sure MongoDB is running
# On Mac: brew services start mongodb-community
# On Linux: sudo systemctl start mongodb
# On Windows: net start MongoDB
```

### 2. Build the Project
```bash
./mvnw clean install -DskipTests
```

### 3. Run the Application
```bash
./mvnw spring-boot:run
```

You should see:
```
Started AssignmentApplication in X seconds
```

## Testing the API

### Option 1: Using the Automated Test Script
```bash
# Make sure the app is running first!
./test-api.sh
```

### Option 2: Manual Testing with CURL

#### 1. Register a Passenger
```bash
curl -X POST http://localhost:8081/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{"username":"john","password":"1234","role":"ROLE_USER"}'
```

Save the token from the response!

#### 2. Register a Driver
```bash
curl -X POST http://localhost:8081/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{"username":"driver1","password":"abcd","role":"ROLE_DRIVER"}'
```

#### 3. Create a Ride (use USER token)
```bash
curl -X POST http://localhost:8081/api/v1/rides \
  -H "Authorization: Bearer YOUR_USER_TOKEN" \
  -H "Content-Type: application/json" \
  -d '{"pickupLocation":"Koramangala","dropLocation":"Indiranagar"}'
```

#### 4. View Pending Rides (use DRIVER token)
```bash
curl -X GET http://localhost:8081/api/v1/driver/rides/requests \
  -H "Authorization: Bearer YOUR_DRIVER_TOKEN"
```

#### 5. Accept a Ride (use DRIVER token and RIDE_ID from step 3)
```bash
curl -X POST http://localhost:8081/api/v1/driver/rides/RIDE_ID/accept \
  -H "Authorization: Bearer YOUR_DRIVER_TOKEN"
```

#### 6. Complete the Ride
```bash
curl -X POST http://localhost:8081/api/v1/rides/RIDE_ID/complete \
  -H "Authorization: Bearer YOUR_DRIVER_TOKEN"
```

### Option 3: Using Postman
Import the `RideShare-Postman-Collection.json` file into Postman.

## Project Structure Overview

```
src/main/java/com/harsha/assignment/
├── model/
│   ├── User.java              # User entity (MongoDB document)
│   └── Ride.java              # Ride entity (MongoDB document)
├── repository/
│   ├── UserRepository.java    # User data access
│   └── RideRepository.java    # Ride data access
├── service/
│   ├── AuthService.java       # Authentication logic
│   └── RideService.java       # Ride business logic
├── controller/
│   ├── AuthController.java    # Auth endpoints
│   └── RideController.java    # Ride endpoints
├── config/
│   ├── SecurityConfig.java    # Spring Security config
│   └── JwtAuthenticationFilter.java  # JWT filter
├── dto/
│   ├── RegisterRequest.java   # Registration input
│   ├── LoginRequest.java      # Login input
│   ├── AuthResponse.java      # Auth output
│   ├── CreateRideRequest.java # Ride creation input
│   └── RideResponse.java      # Ride output
├── exception/
│   ├── GlobalExceptionHandler.java  # Global error handler
│   ├── NotFoundException.java       # Custom exception
│   ├── BadRequestException.java     # Custom exception
│   └── ErrorResponse.java           # Error response DTO
└── util/
    └── JwtUtil.java           # JWT utility methods
```

## API Endpoints Summary

### Public Endpoints
- `POST /api/auth/register` - Register new user
- `POST /api/auth/login` - Login and get JWT token

### Protected Endpoints (Require JWT Token)

**USER (Passenger) Endpoints:**
- `POST /api/v1/rides` - Create ride request
- `GET /api/v1/user/rides` - View my rides
- `POST /api/v1/rides/{id}/complete` - Complete ride

**DRIVER Endpoints:**
- `GET /api/v1/driver/rides/requests` - View pending rides
- `POST /api/v1/driver/rides/{id}/accept` - Accept ride
- `GET /api/v1/driver/rides` - View my accepted rides
- `POST /api/v1/rides/{id}/complete` - Complete ride

## Common Issues & Solutions

### Issue: MongoDB connection refused
**Solution:** Make sure MongoDB is running on localhost:27017

### Issue: Port 8081 already in use
**Solution:** Change port in `application.yaml` or stop the service using port 8081

### Issue: JWT token expired
**Solution:** Login again to get a new token (valid for 24 hours)

### Issue: 403 Forbidden
**Solution:** Check that you're using the correct role's token for the endpoint

## What Each Component Does

### Models
- Define data structure for MongoDB documents
- `User`: Stores user credentials and role
- `Ride`: Stores ride information and status

### Repositories
- Interface with MongoDB
- Provide CRUD operations and custom queries

### Services
- Contain business logic
- `AuthService`: Handles registration, login, password encryption, JWT generation
- `RideService`: Handles ride creation, acceptance, completion

### Controllers
- Expose REST API endpoints
- Validate input using `@Valid`
- Call service methods

### Config
- `SecurityConfig`: Configure Spring Security, define which endpoints require authentication
- `JwtAuthenticationFilter`: Intercept requests, validate JWT tokens

### DTOs
- Data Transfer Objects for API requests/responses
- Include validation annotations

### Exception
- `GlobalExceptionHandler`: Catch exceptions and return formatted error responses
- Custom exceptions for different error scenarios

### Util
- `JwtUtil`: Generate, parse, and validate JWT tokens

## Features Implemented

✅ User registration with role selection
✅ JWT-based authentication
✅ BCrypt password encryption
✅ Role-based authorization (USER, DRIVER)
✅ Request ride functionality
✅ View pending rides
✅ Accept ride requests
✅ Complete rides
✅ Input validation
✅ Global exception handling
✅ Clean architecture
✅ MongoDB integration

## Next Steps

After testing the basic flow, you can:
1. Add more fields to rides (price, distance, etc.)
2. Implement ride cancellation
3. Add ride history endpoint
4. Implement pagination for ride lists
5. Add user profile management
6. Implement ratings and reviews

## Need Help?

Check the full `README.md` for detailed API documentation and examples.

---
**Happy Coding! 🎉**
