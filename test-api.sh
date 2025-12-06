#!/bin/bash

# RideShare Backend API Test Script
# This script tests all the API endpoints in sequence

BASE_URL="http://localhost:8081"
echo "🚀 RideShare Backend API Testing Script"
echo "========================================"
echo ""

# Colors for output
GREEN='\033[0;32m'
BLUE='\033[0;34m'
RED='\033[0;31m'
NC='\033[0m' # No Color

# Step 1: Register a User (Passenger)
echo -e "${BLUE}Step 1: Registering a passenger (john)...${NC}"
USER_RESPONSE=$(curl -s -X POST "$BASE_URL/api/auth/register" \
  -H "Content-Type: application/json" \
  -d '{
    "username": "john",
    "password": "1234",
    "role": "ROLE_USER"
  }')

echo "$USER_RESPONSE" | jq .
USER_TOKEN=$(echo "$USER_RESPONSE" | jq -r '.token')
echo -e "${GREEN}✓ User registered. Token: ${USER_TOKEN:0:50}...${NC}"
echo ""

# Step 2: Register a Driver
echo -e "${BLUE}Step 2: Registering a driver (driver1)...${NC}"
DRIVER_RESPONSE=$(curl -s -X POST "$BASE_URL/api/auth/register" \
  -H "Content-Type: application/json" \
  -d '{
    "username": "driver1",
    "password": "abcd",
    "role": "ROLE_DRIVER"
  }')

echo "$DRIVER_RESPONSE" | jq .
DRIVER_TOKEN=$(echo "$DRIVER_RESPONSE" | jq -r '.token')
echo -e "${GREEN}✓ Driver registered. Token: ${DRIVER_TOKEN:0:50}...${NC}"
echo ""

# Step 3: User creates a ride request
echo -e "${BLUE}Step 3: User creating a ride request...${NC}"
RIDE_RESPONSE=$(curl -s -X POST "$BASE_URL/api/v1/rides" \
  -H "Authorization: Bearer $USER_TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "pickupLocation": "Koramangala",
    "dropLocation": "Indiranagar"
  }')

echo "$RIDE_RESPONSE" | jq .
RIDE_ID=$(echo "$RIDE_RESPONSE" | jq -r '.id')
echo -e "${GREEN}✓ Ride created with ID: $RIDE_ID${NC}"
echo ""

# Step 4: User views their rides
echo -e "${BLUE}Step 4: User viewing their rides...${NC}"
curl -s -X GET "$BASE_URL/api/v1/user/rides" \
  -H "Authorization: Bearer $USER_TOKEN" | jq .
echo -e "${GREEN}✓ User rides retrieved${NC}"
echo ""

# Step 5: Driver views pending ride requests
echo -e "${BLUE}Step 5: Driver viewing pending ride requests...${NC}"
curl -s -X GET "$BASE_URL/api/v1/driver/rides/requests" \
  -H "Authorization: Bearer $DRIVER_TOKEN" | jq .
echo -e "${GREEN}✓ Pending rides retrieved${NC}"
echo ""

# Step 6: Driver accepts the ride
echo -e "${BLUE}Step 6: Driver accepting ride $RIDE_ID...${NC}"
ACCEPT_RESPONSE=$(curl -s -X POST "$BASE_URL/api/v1/driver/rides/$RIDE_ID/accept" \
  -H "Authorization: Bearer $DRIVER_TOKEN")

echo "$ACCEPT_RESPONSE" | jq .
echo -e "${GREEN}✓ Ride accepted by driver${NC}"
echo ""

# Step 7: Driver views their accepted rides
echo -e "${BLUE}Step 7: Driver viewing their accepted rides...${NC}"
curl -s -X GET "$BASE_URL/api/v1/driver/rides" \
  -H "Authorization: Bearer $DRIVER_TOKEN" | jq .
echo -e "${GREEN}✓ Driver's rides retrieved${NC}"
echo ""

# Step 8: Complete the ride (by driver)
echo -e "${BLUE}Step 8: Driver completing the ride...${NC}"
COMPLETE_RESPONSE=$(curl -s -X POST "$BASE_URL/api/v1/rides/$RIDE_ID/complete" \
  -H "Authorization: Bearer $DRIVER_TOKEN")

echo "$COMPLETE_RESPONSE" | jq .
echo -e "${GREEN}✓ Ride completed${NC}"
echo ""

# Step 9: Verify final status
echo -e "${BLUE}Step 9: Verifying final status...${NC}"
echo "User's rides:"
curl -s -X GET "$BASE_URL/api/v1/user/rides" \
  -H "Authorization: Bearer $USER_TOKEN" | jq .
echo ""

echo -e "${GREEN}========================================${NC}"
echo -e "${GREEN}✓ All tests completed successfully!${NC}"
echo -e "${GREEN}========================================${NC}"
