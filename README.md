# 🚗 Ride-Sharing System

A production-ready Spring Boot application with JWT authentication, MongoDB integration, 14 advanced query & analytics APIs, and comprehensive test coverage.

[![Java](https://img.shields.io/badge/Java-21-orange.svg)](https://www.oracle.com/java/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-4.0.0-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![MongoDB](https://img.shields.io/badge/MongoDB-Embedded-green.svg)](https://www.mongodb.com/)
[![Test Coverage](https://img.shields.io/badge/Coverage-85%25+-success.svg)](./TEST_COVERAGE_REPORT.md)

---

## 📋 Table of Contents

- [Quick Start](#-quick-start)
- [Features](#-features)
- [API Documentation](#-api-documentation)
- [Testing](#-testing)
- [Project Structure](#-project-structure)
- [Technology Stack](#-technology-stack)

---

## 🚀 Quick Start

### Prerequisites
- Java 21 or higher
- Maven (optional, embedded wrapper included)
- IDE (IntelliJ IDEA, Eclipse, or VS Code)
- No MongoDB installation needed (Embedded MongoDB included)

### Running the Application

1. **Clone and open the project**
2. **Run the main application:**
   ```bash
   # Using your IDE: Run AssignmentApplication.java
   # Or using command line:
   mvn spring-boot:run
   ```
3. **Application starts on:** http://localhost:8081

### Quick Test

```bash
# Register a user
curl -X POST http://localhost:8081/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{"username":"testuser","password":"pass123","role":"ROLE_USER"}'

# Login and get token
curl -X POST http://localhost:8081/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"testuser","password":"pass123"}'
```

---

## ✨ Features

### Core Functionality
- ✅ **JWT Authentication** - Secure token-based authentication
- ✅ **Role-Based Access** - Passengers (ROLE_USER) and Drivers (ROLE_DRIVER)
- ✅ **Ride Management** - Create, accept, and complete rides
- ✅ **Advanced Search** - 14 powerful query and analytics APIs
- ✅ **MongoDB Queries** - OR, AND, Regex, Range, Date filters
- ✅ **Aggregation Pipelines** - Real-time analytics and metrics
- ✅ **Pagination & Sorting** - Efficient data retrieval
- ✅ **Input Validation** - Comprehensive request validation
- ✅ **Error Handling** - Consistent error responses

### Test Coverage
- ✅ **110+ Tests** - Comprehensive test suite
- ✅ **85%+ Coverage** - High code coverage
- ✅ **All APIs Tested** - Every endpoint validated
- ✅ **CI/CD Ready** - Fast, reliable tests

---

## 📚 API Documentation

### Authentication (2 APIs)
```
POST /api/auth/register  - Register new user
POST /api/auth/login     - Login and get JWT token
```

### Core Ride APIs (6 APIs)
```
POST   /api/v1/rides                      - Create ride (passengers)
POST   /api/v1/rides/{id}/complete        - Complete ride
GET    /api/v1/user/rides                 - Get my rides
GET    /api/v1/driver/rides/requests      - View pending rides (drivers)
POST   /api/v1/driver/rides/{id}/accept   - Accept ride (drivers)
GET    /api/v1/driver/rides                - Get driver's rides
```

### Advanced Query APIs (10 APIs)
```
GET /api/v1/rides/search                           - Search by keyword
GET /api/v1/rides/filter-distance                  - Filter by distance range
GET /api/v1/rides/filter-date-range                - Filter by date range
GET /api/v1/rides/sort                             - Sort by fare
GET /api/v1/rides/user/{userId}                    - Get user's rides
GET /api/v1/rides/user/{userId}/status/{status}    - User rides by status
GET /api/v1/driver/{driverId}/active-rides         - Driver active rides
GET /api/v1/rides/filter-status                    - Status + keyword filter
GET /api/v1/rides/advanced-search                  - Multi-criteria + pagination
GET /api/v1/rides/date/{date}                      - Rides by specific date
```

### Analytics APIs (4 APIs)
```
GET /api/v1/analytics/rides-per-day                - Daily ride statistics
GET /api/v1/analytics/driver/{id}/summary          - Driver performance metrics
GET /api/v1/analytics/user/{id}/spending           - User spending analytics
GET /api/v1/analytics/status-summary               - Status distribution
```

**Total Endpoints:** 22 APIs

📖 **Full API Documentation:** [README_FULL.md](./README_FULL.md)

---

## 🧪 Testing

### Run All Tests
```bash
mvn test
```

### Test Coverage
- **110+ comprehensive tests**
- **85%+ code coverage**
- **All layers tested:** Controllers, Services, Models, DTOs, Utilities

### Test Structure
```
src/test/java/
├── controller/          # REST API tests (31 tests)
├── service/            # Business logic tests (42 tests)
├── util/               # Utility tests (7 tests)
├── model/              # Model tests (13 tests)
├── dto/                # DTO tests (11 tests)
└── exception/          # Exception tests (6 tests)
```

📊 **Detailed Test Report:** [TEST_COVERAGE_REPORT.md](./TEST_COVERAGE_REPORT.md)

---

## 📁 Project Structure

```
src/main/java/com/harsha/assignment/
├── config/              # Security & JWT configuration
├── controller/          # REST API endpoints
│   ├── AuthController.java
│   ├── RideController.java (16 endpoints)
│   └── AnalyticsController.java (4 endpoints)
├── service/             # Business logic
│   ├── AuthService.java
│   ├── RideService.java
│   └── AnalyticsService.java
├── model/               # Data models
│   ├── User.java
│   └── Ride.java
├── dto/                 # Data transfer objects (9 DTOs)
├── repository/          # Data access
├── exception/           # Exception handling
└── util/                # Utilities (JWT)
```

---

## 🛠️ Technology Stack

- **Spring Boot 4.0.0** - Application framework
- **Spring Data MongoDB** - Database integration
- **Spring Security** - JWT authentication
- **Embedded MongoDB** - No installation needed
- **Lombok** - Reduce boilerplate
- **JUnit 5 & Mockito** - Testing frameworks
- **Maven** - Build tool

---

## 📊 Key Metrics

| Metric | Value |
|--------|-------|
| Total APIs | 22 endpoints |
| Code Files | 27 Java files |
| Test Files | 12 test classes |
| Test Cases | 110+ tests |
| Code Coverage | 85%+ |
| Lines of Code | 2000+ |

---

## 🎯 MongoDB Features

### Advanced Queries
- OR and AND operations
- Regex pattern matching (case-insensitive)
- Range queries (gte/lte)
- Date filtering
- Sorting and pagination

### Aggregation Pipelines
- $match - Document filtering
- $group - Grouping operations
- $project - Output shaping
- $sort - Result ordering
- Statistical functions (sum, avg, count)

---

## 🔒 Security

- **JWT Token Authentication** - Stateless authentication
- **Role-Based Access Control** - ROLE_USER and ROLE_DRIVER
- **Password Encryption** - BCrypt hashing
- **Token Expiration** - 24 hours (configurable)
- **Secured Endpoints** - All APIs require authentication except /api/auth/*

---

## 📝 Data Models

### User
```java
{
  "id": "String",
  "username": "String (unique)",
  "password": "String (encrypted)",
  "role": "ROLE_USER | ROLE_DRIVER"
}
```

### Ride
```java
{
  "id": "String",
  "userId": "String",
  "driverId": "String (nullable)",
  "pickupLocation": "String",
  "dropLocation": "String",
  "status": "REQUESTED | ACCEPTED | COMPLETED | CANCELLED",
  "fareAmount": "Double",
  "distanceKm": "Double",
  "createdAt": "LocalDateTime",
  "createdDate": "LocalDate"
}
```

---

## 🚦 Ride Status Flow

```
REQUESTED → ACCEPTED → COMPLETED
           ↓
        CANCELLED
```

---

## 🎓 Documentation Files

| File | Description |
|------|-------------|
| `README.md` | This file - Quick overview |
| `README_FULL.md` | Complete documentation with all API details |
| `TEST_COVERAGE_REPORT.md` | Comprehensive test coverage report |

---

## 🐛 Troubleshooting

### Application won't start?
- Ensure Java 21+ is installed
- Check port 8081 is not in use
- Verify Maven dependencies are downloaded

### 401 Unauthorized?
- Ensure you have a valid JWT token
- Include header: `Authorization: Bearer {token}`
- Token expires after 24 hours - login again

### Tests failing?
- Run `mvn clean test`
- Check Java version: `java -version`

---

## 🎯 Getting Started Checklist

- [ ] Clone/open the project
- [ ] Run the application
- [ ] Register a user (passenger and driver)
- [ ] Login and get JWT token
- [ ] Create a ride
- [ ] Test search APIs
- [ ] Check analytics endpoints
- [ ] Run tests: `mvn test`

---

## 📈 Performance

- **Startup Time:** 3-5 seconds
- **API Response:** < 100ms (simple queries)
- **API Response:** < 500ms (complex aggregations)
- **Test Execution:** < 10 seconds

---

## 🤝 Contributing

This is an educational project demonstrating:
- Spring Boot REST API development
- MongoDB advanced querying
- JWT authentication
- Comprehensive testing
- Clean architecture
- Best practices

---

## 📞 Support

For detailed information:
- **Full API Documentation:** [README_FULL.md](./README_FULL.md)
- **Test Coverage Details:** [TEST_COVERAGE_REPORT.md](./TEST_COVERAGE_REPORT.md)

---

## ✨ Status

✅ **Production Ready**
- All features implemented
- Comprehensive tests (85%+ coverage)
- Fully documented
- CI/CD ready

---

**Built with ❤️ using Spring Boot**

