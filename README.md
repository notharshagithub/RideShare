# Assignment 1


# 🚀 RideShare Backend - Spring Boot Application

A complete ride-sharing backend application built with Spring Boot, MongoDB, JWT Authentication, and comprehensive validation.

## 📋 Features

- ✅ User Registration & Login with JWT Authentication
- ✅ Role-based Authorization (ROLE_USER and ROLE_DRIVER)
- ✅ Passengers can request rides
- ✅ Drivers can view and accept ride requests
- ✅ Both users and drivers can complete rides
- ✅ Input validation with Jakarta Bean Validation
- ✅ Global exception handling
- ✅ BCrypt password encoding
- ✅ Clean architecture (Controller → Service → Repository)

## 🏗️ Technology Stack

- **Java 21**
- **Spring Boot 4.0.0**
- **MongoDB** - NoSQL Database
- **Spring Security** - Authentication & Authorization
- **JWT (jjwt 0.12.5)** - Token-based authentication
- **Lombok** - Reduce boilerplate code
- **Jakarta Validation** - Input validation

## 📁 Project Structure

```
src/main/java/com/harsha/assignment/
├── model/              # Entity classes (User, Ride)
├── repository/         # MongoDB repositories
├── service/           # Business logic layer
├── controller/        # REST API endpoints
├── config/            # Security & JWT configuration
├── dto/               # Data Transfer Objects
├── exception/         # Custom exceptions & global handler
└── util/              # Utility classes (JwtUtil)
```

## 🗄️ Database Schema

### User Entity
```
{
  "id": "String",
  "username": "String (unique)",
  "password": "String (BCrypt encoded)",
  "role": "String (ROLE_USER or ROLE_DRIVER)"
}
```

### Ride Entity
```
{
  "id": "String",
  "userId": "String (FK to User)",
  "driverId": "String (FK to User, nullable)",
  "pickupLocation": "String",
  "dropLocation": "String",
  "status": "String (REQUESTED, ACCEPTED, COMPLETED)",
  "createdAt": "LocalDateTime"
}
```

## 🚀 Getting Started

### Prerequisites

1. Java 21 or higher
2. Maven 3.6+
3. MongoDB running on `localhost:27017`

### Installation

1. Clone the repository
2. Update MongoDB connection in `src/main/resources/application.yaml` if needed
3. Build the project:
   ```bash
   mvn clean install
   ```
4. Run the application:
   ```bash
   mvn spring-boot:run
   ```

The application will start on `http://localhost:8081`

## 📡 API Endpoints

### Authentication Endpoints (Public)

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/auth/register` | Register a new user |
| POST | `/api/auth/login` | Login and get JWT token |

### Ride Endpoints (Authenticated)

| Method | Endpoint | Role | Description |
|--------|----------|------|-------------|
| POST | `/api/v1/rides` | USER | Create a new ride request |
| GET | `/api/v1/user/rides` | USER | Get user's own rides |
| GET | `/api/v1/driver/rides/requests` | DRIVER | View all pending ride requests |
| POST | `/api/v1/driver/rides/{rideId}/accept` | DRIVER | Accept a ride request |
| GET | `/api/v1/driver/rides` | DRIVER | Get driver's accepted rides |
| POST | `/api/v1/rides/{rideId}/complete` | USER/DRIVER | Complete a ride |

## 🧪 Testing with CURL

### 1. Register a Passenger (USER)

```bash
curl -X POST http://localhost:8081/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "username": "john",
    "password": "1234",
    "role": "ROLE_USER"
  }'
```

**Response:**
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "username": "john",
  "role": "ROLE_USER"
}
```

### 2. Register a Driver

```bash
curl -X POST http://localhost:8081/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "username": "driver1",
    "password": "abcd",
    "role": "ROLE_DRIVER"
  }'
```

### 3. Login

```bash
curl -X POST http://localhost:8081/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "john",
    "password": "1234"
  }'
```

### 4. Create a Ride (USER)

```bash
curl -X POST http://localhost:8081/api/v1/rides \
  -H "Authorization: Bearer YOUR_USER_TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "pickupLocation": "Koramangala",
    "dropLocation": "Indiranagar"
  }'
```

**Response:**
```json
{
  "id": "65abc123...",
  "userId": "65abc456...",
  "driverId": null,
  "pickupLocation": "Koramangala",
  "dropLocation": "Indiranagar",
  "status": "REQUESTED",
  "createdAt": "2025-01-20T12:00:00"
}
```

### 5. View User's Rides

```bash
curl -X GET http://localhost:8081/api/v1/user/rides \
  -H "Authorization: Bearer YOUR_USER_TOKEN"
```

### 6. View Pending Rides (DRIVER)

```bash
curl -X GET http://localhost:8081/api/v1/driver/rides/requests \
  -H "Authorization: Bearer YOUR_DRIVER_TOKEN"
```

### 7. Accept a Ride (DRIVER)

```bash
curl -X POST http://localhost:8081/api/v1/driver/rides/RIDE_ID/accept \
  -H "Authorization: Bearer YOUR_DRIVER_TOKEN"
```

### 8. Complete a Ride

```bash
curl -X POST http://localhost:8081/api/v1/rides/RIDE_ID/complete \
  -H "Authorization: Bearer YOUR_TOKEN"
```

### 9. View Driver's Accepted Rides

```bash
curl -X GET http://localhost:8081/api/v1/driver/rides \
  -H "Authorization: Bearer YOUR_DRIVER_TOKEN"
```

## 🔐 JWT Token Structure

The JWT token contains:
- **username**: User's username
- **role**: User's role (ROLE_USER or ROLE_DRIVER)
- **issuedAt**: Token creation timestamp
- **expiration**: Token expiry (24 hours from creation)

Include the token in the `Authorization` header:
```
Authorization: Bearer <your_jwt_token>
```

## ✅ Validation Rules

### RegisterRequest
- `username`: Required, minimum 3 characters
- `password`: Required, minimum 4 characters
- `role`: Required, must be "ROLE_USER" or "ROLE_DRIVER"

### LoginRequest
- `username`: Required
- `password`: Required

### CreateRideRequest
- `pickupLocation`: Required, not blank
- `dropLocation`: Required, not blank

## 🚨 Error Responses

### Validation Error
```json
{
  "error": "VALIDATION_ERROR",
  "message": "{fieldName=error message}",
  "timestamp": "2025-01-20T12:00:00"
}
```

### Not Found Error
```json
{
  "error": "NOT_FOUND",
  "message": "User not found",
  "timestamp": "2025-01-20T12:00:00"
}
```

### Bad Request Error
```json
{
  "error": "BAD_REQUEST",
  "message": "Only drivers can accept rides",
  "timestamp": "2025-01-20T12:00:00"
}
```

## 🎯 Business Logic

1. **User Registration**: 
   - Validates role is either ROLE_USER or ROLE_DRIVER
   - Checks username uniqueness
   - Encrypts password with BCrypt
   - Returns JWT token

2. **Ride Request (USER)**:
   - Only users with ROLE_USER can request rides
   - Creates ride with status "REQUESTED"
   - Stores userId of the passenger

3. **View Pending Rides (DRIVER)**:
   - Returns all rides with status "REQUESTED"

4. **Accept Ride (DRIVER)**:
   - Only users with ROLE_DRIVER can accept rides
   - Ride must be in "REQUESTED" status
   - Assigns driverId and changes status to "ACCEPTED"

5. **Complete Ride**:
   - Ride must be in "ACCEPTED" status
   - Can be completed by either the passenger or driver
   - Changes status to "COMPLETED"

## 📝 Configuration

Edit `src/main/resources/application.yaml`:

```yaml
spring:
  data:
    mongodb:
      uri: mongodb://localhost:27017/rideshare

server:
  port: 8081

jwt:
  secret: your-secret-key
  expiration: 86400000  # 24 hours
```

## 🔧 Development

### Build the project
```bash
mvn clean install
```

### Run tests
```bash
mvn test
```

### Run the application
```bash
mvn spring-boot:run
```

## 📦 Dependencies

- spring-boot-starter-web
- spring-boot-starter-data-mongodb
- spring-boot-starter-security
- spring-boot-starter-validation
- jjwt (JWT library)
- lombok

## 👥 Roles & Permissions

### ROLE_USER (Passenger)
- ✅ Can request rides
- ✅ Can view their own rides
- ✅ Can complete their rides

### ROLE_DRIVER
- ✅ Can view pending ride requests
- ✅ Can accept ride requests
- ✅ Can view their accepted rides
- ✅ Can complete rides

## 🌟 Project Highlights

- **Clean Architecture**: Separation of concerns with clear layers
- **Security**: JWT-based authentication with BCrypt password encoding
- **Validation**: Comprehensive input validation at DTO level
- **Exception Handling**: Global exception handler for consistent error responses
- **RESTful API**: Follows REST principles
- **MongoDB Integration**: NoSQL database for flexible data storage

## 📄 License

This project is created for educational purposes.

## 👨‍💻 Author

Harsha - RideShare Backend Project

---

**Happy Coding! 🚀**
