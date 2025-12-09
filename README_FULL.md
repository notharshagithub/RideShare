# 🚗 Ride-Sharing System - Complete Guide

A production-ready Spring Boot application with JWT authentication, MongoDB integration, and 14 advanced query & analytics APIs.

---

## 📑 Table of Contents

1. [Quick Setup Guide](#-quick-setup-guide)
2. [Project Overview](#-project-overview)
3. [Features](#-features)
4. [Technology Stack](#-technology-stack)
5. [Project Structure](#-project-structure)
6. [API Documentation](#-api-documentation)
7. [Testing Guide](#-testing-guide)
8. [Architecture](#-architecture)

---

## 🚀 Quick Setup Guide

### Prerequisites
- Java 21 or higher
- IDE (IntelliJ IDEA, Eclipse, or VS Code)
- No MongoDB installation needed (Embedded MongoDB included)

### Step 1: Run the Application

**Option A: Using IDE**
```
1. Open the project in your IDE
2. Navigate to: src/main/java/com/harsha/assignment/AssignmentApplication.java
3. Right-click and select "Run" or press the Run button
4. Wait for startup (3-5 seconds)
```

**Option B: Using Maven (if available)**
```bash
mvn spring-boot:run
```

### Step 2: Verify Startup

Look for these messages in the console:
```
✅ Started AssignmentApplication in X.XXX seconds
✅ Tomcat started on port(s): 8081 (http)
✅ Embedded MongoDB started successfully
```

The application will be available at: **http://localhost:8081**

### Step 3: Quick Test

**Register a user:**
```bash
curl -X POST http://localhost:8081/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "username": "testuser",
    "password": "pass123",
    "role": "ROLE_USER"
  }'
```

**Login and get token:**
```bash
curl -X POST http://localhost:8081/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "testuser",
    "password": "pass123"
  }'
```

Save the token from the response and use it in subsequent requests:
```bash
Authorization: Bearer YOUR_TOKEN_HERE
```

---

## 📋 Project Overview

### What is This?

A comprehensive ride-sharing backend system (like Uber) with:
- **User Management**: Passengers (ROLE_USER) and Drivers (ROLE_DRIVER)
- **JWT Authentication**: Secure token-based authentication
- **Ride Management**: Create, accept, and complete rides
- **Advanced Search**: 14 powerful query and analytics APIs
- **MongoDB Integration**: Complex queries and aggregation pipelines

### What's Included?

- ✅ 22 REST API endpoints (8 core + 14 advanced)
- ✅ JWT security with role-based access control
- ✅ MongoDB queries (OR, AND, Regex, Range, Date filters)
- ✅ MongoDB aggregations (Group, Match, Project, Sort)
- ✅ Pagination and sorting support
- ✅ Input validation and error handling
- ✅ Embedded MongoDB (no installation required)

---

## ✨ Features

### Core Features

**1. Authentication & Authorization**
- User registration with role selection (Passenger or Driver)
- JWT token-based authentication
- Secure endpoints with role-based access control
- Token expiration (24 hours, configurable)

**2. Ride Management**
- Passengers can create ride requests with pickup/drop locations, fare, and distance
- Drivers can view pending ride requests
- Drivers can accept rides
- Users can complete rides
- Track ride status (REQUESTED → ACCEPTED → COMPLETED)

**3. Advanced Search & Filtering**
- Search rides by location keywords
- Filter by distance range
- Filter by date range
- Sort by fare amount
- Get rides for specific users
- Filter by status
- Paginated results
- Combined AND/OR queries

**4. Analytics Dashboard**
- Rides per day statistics
- Driver performance metrics (total rides, completed, cancelled, avg distance, total fare)
- User spending analytics
- Status distribution (rides grouped by status)

---

## 🛠️ Technology Stack

### Backend
- **Spring Boot 4.0.0** - Application framework
- **Spring Data MongoDB** - Database integration
- **Spring Security** - JWT authentication & authorization
- **Spring Validation** - Input validation

### Database
- **Embedded MongoDB 4.7.0** - NoSQL database (automatically starts with app)

### Security
- **JJWT 0.12.5** - JWT token generation and validation

### Utilities
- **Lombok** - Reduce boilerplate code
- **Jakarta Validation** - Bean validation

### Build Tool
- **Maven** - Dependency management

---

## 📁 Project Structure

```
src/main/java/com/harsha/assignment/
│
├── 📂 config/                          # Security & JWT Configuration
│   ├── JwtAuthenticationFilter.java   # JWT token validation filter
│   └── SecurityConfig.java            # Security configuration
│
├── 📂 controller/                      # REST API Controllers
│   ├── AuthController.java            # Authentication endpoints (register, login)
│   ├── RideController.java            # Ride management + 10 query APIs
│   └── AnalyticsController.java       # 4 analytics APIs
│
├── 📂 service/                         # Business Logic Layer
│   ├── AuthService.java               # Authentication service
│   ├── RideService.java               # Ride management + query methods
│   └── AnalyticsService.java          # Analytics with aggregation pipelines
│
├── 📂 model/                           # Data Models
│   ├── User.java                      # User entity (Passenger/Driver)
│   └── Ride.java                      # Ride entity with fare & distance
│
├── 📂 dto/                             # Data Transfer Objects
│   ├── AuthResponse.java              # Login response
│   ├── LoginRequest.java              # Login request
│   ├── RegisterRequest.java           # Registration request
│   ├── CreateRideRequest.java         # Create ride request
│   ├── RideResponse.java              # Ride response
│   ├── RidesPerDayResponse.java       # Analytics DTO
│   ├── DriverSummaryResponse.java     # Analytics DTO
│   ├── UserSpendingResponse.java      # Analytics DTO
│   └── StatusSummaryResponse.java     # Analytics DTO
│
├── 📂 repository/                      # Data Access Layer
│   ├── UserRepository.java            # User data access
│   └── RideRepository.java            # Ride data access
│
├── 📂 exception/                       # Exception Handling
│   ├── BadRequestException.java       # 400 errors
│   ├── NotFoundException.java         # 404 errors
│   ├── ErrorResponse.java             # Error response structure
│   └── GlobalExceptionHandler.java    # Global exception handler
│
└── 📂 util/                            # Utility Classes
    └── JwtUtil.java                   # JWT token utilities

src/main/resources/
└── application.yaml                   # Application configuration
```

---

## 📚 API Documentation

### Base URL
```
http://localhost:8081
```

### Authentication APIs

#### 1. Register User
```http
POST /api/auth/register
Content-Type: application/json

{
  "username": "john_passenger",
  "password": "password123",
  "role": "ROLE_USER"  // or "ROLE_DRIVER"
}
```

**Response:**
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "username": "john_passenger",
  "role": "ROLE_USER"
}
```

#### 2. Login
```http
POST /api/auth/login
Content-Type: application/json

{
  "username": "john_passenger",
  "password": "password123"
}
```

**Response:**
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "username": "john_passenger",
  "role": "ROLE_USER"
}
```

---

### Core Ride APIs

#### 3. Create Ride (Passengers only)
```http
POST /api/v1/rides
Authorization: Bearer {token}
Content-Type: application/json

{
  "pickupLocation": "Koramangala",
  "dropLocation": "Whitefield",
  "fareAmount": 250.0,
  "distanceKm": 15.5
}
```

#### 4. View Pending Rides (Drivers only)
```http
GET /api/v1/driver/rides/requests
Authorization: Bearer {token}
```

#### 5. Accept Ride (Drivers only)
```http
POST /api/v1/driver/rides/{rideId}/accept
Authorization: Bearer {token}
```

#### 6. Complete Ride
```http
POST /api/v1/rides/{rideId}/complete
Authorization: Bearer {token}
```

#### 7. Get My Rides
```http
GET /api/v1/user/rides
Authorization: Bearer {token}
```

#### 8. Get Driver's Rides
```http
GET /api/v1/driver/rides
Authorization: Bearer {token}
```

---

### Advanced Query APIs (14 APIs)

#### API 1: Search by Keyword
**Search rides by pickup OR drop location (case-insensitive)**
```http
GET /api/v1/rides/search?text=kor
Authorization: Bearer {token}
```

**Features:** OR query + Regex + Case-insensitive

---

#### API 2: Filter by Distance Range
**Get rides within a distance range**
```http
GET /api/v1/rides/filter-distance?min=5&max=20
Authorization: Bearer {token}
```

**Features:** Range query (gte/lte)

---

#### API 3: Filter by Date Range
**Get rides between two dates**
```http
GET /api/v1/rides/filter-date-range?start=2025-01-01&end=2025-01-31
Authorization: Bearer {token}
```

**Features:** Date comparison + AND criteria

---

#### API 4: Sort by Fare
**Sort rides by fare amount**
```http
GET /api/v1/rides/sort?order=desc
Authorization: Bearer {token}
```

**Parameters:**
- `order`: `asc` or `desc` (default: `asc`)

**Features:** Sorting in MongoDB

---

#### API 5: Get User Rides
**Get all rides for a specific user**
```http
GET /api/v1/rides/user/{userId}
Authorization: Bearer {token}
```

**Features:** Simple equality filter

---

#### API 6: Get User Rides by Status
**Filter user's rides by status**
```http
GET /api/v1/rides/user/{userId}/status/{status}
Authorization: Bearer {token}
```

**Status values:** `REQUESTED`, `ACCEPTED`, `COMPLETED`, `CANCELLED`

**Features:** AND query

---

#### API 7: Get Driver Active Rides
**Get all active (ACCEPTED) rides for a driver**
```http
GET /api/v1/driver/{driverId}/active-rides
Authorization: Bearer {token}
```

**Features:** Equality + fixed status filter

---

#### API 8: Filter by Status + Keyword
**Combine status filter with location keyword search**
```http
GET /api/v1/rides/filter-status?status=COMPLETED&search=kor
Authorization: Bearer {token}
```

**Features:** AND + OR combo

---

#### API 9: Advanced Search with Pagination
**Multi-criteria search with sorting and pagination**
```http
GET /api/v1/rides/advanced-search?search=kor&status=REQUESTED&sort=fareAmount&order=asc&page=0&size=10
Authorization: Bearer {token}
```

**Parameters:**
- `search` (optional): Keyword to search in pickup/drop locations
- `status` (optional): Filter by ride status
- `sort` (optional, default: `fareAmount`): Field to sort by
- `order` (optional, default: `asc`): Sort order (`asc` or `desc`)
- `page` (optional, default: `0`): Page number
- `size` (optional, default: `10`): Page size

**Features:** Multi-criteria + Pagination + Sorting

---

#### API 10: Rides Per Day (Analytics)
**Get count of rides grouped by date**
```http
GET /api/v1/analytics/rides-per-day
Authorization: Bearer {token}
```

**Response:**
```json
[
  {
    "date": "2025-01-15",
    "count": 45
  },
  {
    "date": "2025-01-14",
    "count": 38
  }
]
```

**Features:** Aggregation (group + sort)

---

#### API 11: Driver Summary (Analytics)
**Get comprehensive driver statistics**
```http
GET /api/v1/analytics/driver/{driverId}/summary
Authorization: Bearer {token}
```

**Response:**
```json
{
  "driverId": "507f1f77bcf86cd799439022",
  "totalRides": 150,
  "completedRides": 140,
  "cancelledRides": 10,
  "avgDistance": 12.5,
  "totalFare": 35000.0
}
```

**Features:** Aggregation (match + group + project + metrics)

---

#### API 12: User Spending (Analytics)
**Get total spending and completed rides for a user**
```http
GET /api/v1/analytics/user/{userId}/spending
Authorization: Bearer {token}
```

**Response:**
```json
{
  "userId": "507f1f77bcf86cd799439011",
  "totalCompletedRides": 50,
  "totalSpent": 12500.0
}
```

**Features:** Aggregation (match + sum + count)

---

#### API 13: Status Summary (Analytics)
**Get count of rides grouped by status**
```http
GET /api/v1/analytics/status-summary
Authorization: Bearer {token}
```

**Response:**
```json
[
  {
    "status": "COMPLETED",
    "count": 500
  },
  {
    "status": "REQUESTED",
    "count": 45
  },
  {
    "status": "ACCEPTED",
    "count": 30
  }
]
```

**Features:** Aggregation (group by status)

---

#### API 14: Get Rides by Date
**Get all rides on a specific date**
```http
GET /api/v1/rides/date/{date}
Authorization: Bearer {token}
```

**Example:**
```http
GET /api/v1/rides/date/2025-01-15
```

**Features:** LocalDate equality matching

---

## 🧪 Testing Guide

### Complete Testing Workflow

#### Step 1: Register Users

**Register a Passenger:**
```bash
curl -X POST http://localhost:8081/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "username": "alice_passenger",
    "password": "pass123",
    "role": "ROLE_USER"
  }'
```

**Register a Driver:**
```bash
curl -X POST http://localhost:8081/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "username": "bob_driver",
    "password": "pass123",
    "role": "ROLE_DRIVER"
  }'
```

#### Step 2: Login and Get Tokens

**Login as Passenger:**
```bash
curl -X POST http://localhost:8081/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "alice_passenger",
    "password": "pass123"
  }'
```

Save the token:
```bash
TOKEN_PASSENGER="eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
```

**Login as Driver:**
```bash
curl -X POST http://localhost:8081/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "bob_driver",
    "password": "pass123"
  }'
```

Save the token:
```bash
TOKEN_DRIVER="eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
```

#### Step 3: Create Test Rides

**Create Ride 1:**
```bash
curl -X POST http://localhost:8081/api/v1/rides \
  -H "Authorization: Bearer $TOKEN_PASSENGER" \
  -H "Content-Type: application/json" \
  -d '{
    "pickupLocation": "Koramangala",
    "dropLocation": "Whitefield",
    "fareAmount": 250.0,
    "distanceKm": 15.5
  }'
```

**Create Ride 2:**
```bash
curl -X POST http://localhost:8081/api/v1/rides \
  -H "Authorization: Bearer $TOKEN_PASSENGER" \
  -H "Content-Type: application/json" \
  -d '{
    "pickupLocation": "Indiranagar",
    "dropLocation": "Koramangala",
    "fareAmount": 150.0,
    "distanceKm": 8.2
  }'
```

**Create Ride 3:**
```bash
curl -X POST http://localhost:8081/api/v1/rides \
  -H "Authorization: Bearer $TOKEN_PASSENGER" \
  -H "Content-Type: application/json" \
  -d '{
    "pickupLocation": "Marathahalli",
    "dropLocation": "Electronic City",
    "fareAmount": 350.0,
    "distanceKm": 22.0
  }'
```

#### Step 4: Test Search APIs

**Search for 'kor':**
```bash
curl -X GET "http://localhost:8081/api/v1/rides/search?text=kor" \
  -H "Authorization: Bearer $TOKEN_PASSENGER"
```

**Filter by distance (5-20 km):**
```bash
curl -X GET "http://localhost:8081/api/v1/rides/filter-distance?min=5&max=20" \
  -H "Authorization: Bearer $TOKEN_PASSENGER"
```

**Sort by fare (descending):**
```bash
curl -X GET "http://localhost:8081/api/v1/rides/sort?order=desc" \
  -H "Authorization: Bearer $TOKEN_PASSENGER"
```

#### Step 5: Driver Accepts Ride

**Driver views pending rides:**
```bash
curl -X GET "http://localhost:8081/api/v1/driver/rides/requests" \
  -H "Authorization: Bearer $TOKEN_DRIVER"
```

**Driver accepts a ride (use ride ID from above):**
```bash
curl -X POST "http://localhost:8081/api/v1/driver/rides/{RIDE_ID}/accept" \
  -H "Authorization: Bearer $TOKEN_DRIVER"
```

#### Step 6: Test Analytics

**Rides per day:**
```bash
curl -X GET "http://localhost:8081/api/v1/analytics/rides-per-day" \
  -H "Authorization: Bearer $TOKEN_PASSENGER"
```

**Status summary:**
```bash
curl -X GET "http://localhost:8081/api/v1/analytics/status-summary" \
  -H "Authorization: Bearer $TOKEN_PASSENGER"
```

**Driver summary (use driver ID):**
```bash
curl -X GET "http://localhost:8081/api/v1/analytics/driver/{DRIVER_ID}/summary" \
  -H "Authorization: Bearer $TOKEN_DRIVER"
```

---

## 🏗️ Architecture

### System Architecture

```
┌─────────────────────────────────────────────────────────────┐
│                         Client Layer                         │
│        (Web Browser / Mobile App / API Testing Tool)        │
└──────────────────────────┬──────────────────────────────────┘
                           │
                           │ HTTP/REST + JWT
                           │
┌──────────────────────────▼──────────────────────────────────┐
│                    Spring Security Layer                     │
│              (JWT Authentication Filter)                     │
└──────────────────────────┬──────────────────────────────────┘
                           │
        ┌──────────────────┼──────────────────┐
        │                  │                  │
┌───────▼────────┐ ┌──────▼────────┐ ┌──────▼────────────┐
│     Auth       │ │     Ride      │ │   Analytics       │
│  Controller    │ │  Controller   │ │   Controller      │
└───────┬────────┘ └──────┬────────┘ └──────┬────────────┘
        │                 │                 │
┌───────▼────────┐ ┌──────▼────────┐ ┌──────▼────────────┐
│     Auth       │ │     Ride      │ │   Analytics       │
│   Service      │ │   Service     │ │   Service         │
└───────┬────────┘ └──────┬────────┘ └──────┬────────────┘
        │                 │                 │
┌───────▼────────┐ ┌──────▼────────┐        │
│     User       │ │     Ride      │        │
│  Repository    │ │  Repository   │        │
└───────┬────────┘ └──────┬────────┘        │
        │                 │                 │
        └─────────────────┼─────────────────┘
                          │
                ┌─────────▼─────────┐
                │   MongoTemplate   │
                │  (Advanced Query) │
                └─────────┬─────────┘
                          │
                ┌─────────▼─────────┐
                │  Embedded MongoDB │
                │   (Data Storage)  │
                └───────────────────┘
```

### Data Flow

**1. Authentication Flow:**
```
User → Register/Login → AuthController → AuthService 
  → Generate JWT Token → Return to User
```

**2. Create Ride Flow:**
```
Passenger → Create Ride Request → RideController → RideService 
  → Validate User Role → Save to MongoDB → Return Ride Response
```

**3. Search Flow:**
```
User → Search Request → RideController → RideService 
  → Build MongoDB Query → Execute with MongoTemplate 
  → Return Filtered Results
```

**4. Analytics Flow:**
```
User → Analytics Request → AnalyticsController → AnalyticsService 
  → Build Aggregation Pipeline → Execute on MongoDB 
  → Return Aggregated Data
```

### Data Models

**User Model:**
```java
{
  "id": "String",
  "username": "String (unique)",
  "password": "String (encrypted)",
  "role": "String (ROLE_USER | ROLE_DRIVER)"
}
```

**Ride Model:**
```java
{
  "id": "String",
  "userId": "String (passenger)",
  "driverId": "String (nullable)",
  "pickupLocation": "String",
  "dropLocation": "String",
  "status": "String (REQUESTED | ACCEPTED | COMPLETED | CANCELLED)",
  "createdAt": "LocalDateTime",
  "createdDate": "LocalDate",
  "fareAmount": "Double",
  "distanceKm": "Double"
}
```

### Security Flow

```
1. User registers/logs in → Receives JWT token
2. User includes token in Authorization header
3. JwtAuthenticationFilter intercepts request
4. Filter validates token and extracts username/role
5. SecurityContext is set with user details
6. Controller method executes with proper authorization
```

---

## 📊 API Summary Table

| # | Endpoint | Method | Type | Feature |
|---|----------|--------|------|---------|
| 1 | `/api/v1/rides/search` | GET | Query | OR + Regex |
| 2 | `/api/v1/rides/filter-distance` | GET | Query | Range Filter |
| 3 | `/api/v1/rides/filter-date-range` | GET | Query | Date Range |
| 4 | `/api/v1/rides/sort` | GET | Query | Sorting |
| 5 | `/api/v1/rides/user/{userId}` | GET | Query | Equality |
| 6 | `/api/v1/rides/user/{userId}/status/{status}` | GET | Query | AND Query |
| 7 | `/api/v1/driver/{driverId}/active-rides` | GET | Query | Status Filter |
| 8 | `/api/v1/rides/filter-status` | GET | Query | AND + OR |
| 9 | `/api/v1/rides/advanced-search` | GET | Query | Pagination |
| 10 | `/api/v1/analytics/rides-per-day` | GET | Analytics | Group + Sort |
| 11 | `/api/v1/analytics/driver/{id}/summary` | GET | Analytics | Metrics |
| 12 | `/api/v1/analytics/user/{id}/spending` | GET | Analytics | Sum + Count |
| 13 | `/api/v1/analytics/status-summary` | GET | Analytics | Group By |
| 14 | `/api/v1/rides/date/{date}` | GET | Query | Date Match |

---

## 🔧 Configuration

### Application Properties (application.yaml)

```yaml
spring:
  application:
    name: rideshare-backend
  data:
    mongodb:
      database: rideshare
  mongodb:
    embedded:
      version: 6.0.6
  
server:
  port: 8081

jwt:
  secret: 5367566B59703373367639792F423F4528482B4D6251655468576D5A71347437
  expiration: 86400000  # 24 hours in milliseconds
```

### Security Configuration

- JWT token expiration: 24 hours (configurable)
- Password encryption: BCrypt
- Public endpoints: `/api/auth/**`
- Protected endpoints: All other endpoints require authentication

---

## 🐛 Troubleshooting

### Application Won't Start

**Problem:** Port 8081 already in use  
**Solution:** Change port in `application.yaml` or stop the process using port 8081

**Problem:** Java version mismatch  
**Solution:** Ensure Java 21 or higher is installed

### Authentication Issues

**Problem:** 401 Unauthorized  
**Solution:** 
- Ensure you're logged in and have a valid token
- Check token is included in `Authorization: Bearer {token}` header
- Token expires after 24 hours - login again if needed

**Problem:** 403 Forbidden  
**Solution:**
- Check user role is correct
- ROLE_USER can create rides
- ROLE_DRIVER can accept rides

### Empty Results

**Problem:** APIs return empty arrays  
**Solution:**
- This is normal if no data exists
- Create some test rides first
- Analytics will show zeros if no data

---

## 📈 Performance Notes

### Expected Performance
- Startup time: 3-5 seconds
- API response time: < 100ms (simple queries)
- API response time: < 500ms (complex aggregations)

### Optimization Tips
1. Add MongoDB indexes on frequently queried fields
2. Use pagination for large datasets
3. Cache analytics results if called frequently
4. Monitor query performance with logs

---

## 🎓 Learning Outcomes

This project demonstrates:
- ✅ Spring Boot REST API development
- ✅ MongoDB advanced querying (OR, AND, Regex, Range, Date)
- ✅ MongoDB aggregation pipelines (Group, Match, Project, Sort)
- ✅ JWT authentication & authorization
- ✅ Role-based access control
- ✅ Input validation & error handling
- ✅ Clean architecture (Controller → Service → Repository)
- ✅ DTO pattern for API contracts
- ✅ Pagination and sorting
- ✅ API design best practices

---

## 📝 License

This project is for educational purposes.

---

## 👨‍💻 Author

Harsha - Ride-Sharing System

---

## 🎯 Summary

**Total Endpoints:** 22 (8 core + 14 advanced)  
**Query Types:** 8 different MongoDB query patterns  
**Aggregation Pipelines:** 4 analytics endpoints  
**Authentication:** JWT-based with role management  
**Database:** Embedded MongoDB (no installation needed)  
**Status:** Production-ready ✅

**Ready to run with zero configuration! 🚀**
