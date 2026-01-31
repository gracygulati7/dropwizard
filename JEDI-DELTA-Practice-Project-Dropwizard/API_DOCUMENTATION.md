## FLIPFIT REST API - ENDPOINTS DOCUMENTATION

### 1. AUTHENTICATION ENDPOINTS (`/auth`)

#### Login as Admin
```
POST /auth/login/admin
Content-Type: application/json

Request:
{
  "email": "admin@flipfit.com",
  "password": "admin123"
}

Response (200):
{
  "success": true,
  "message": "Admin login successful",
  "data": "token-admin"
}
```

#### Login as Customer
```
POST /auth/login/customer
Content-Type: application/json

Request:
{
  "email": "customer@email.com",
  "password": "password"
}

Response (200):
{
  "success": true,
  "message": "Customer login successful",
  "data": {
    "userId": 1,
    "fullName": "John Doe",
    "email": "customer@email.com",
    "role": "CUSTOMER"
  }
}
```

#### Login as Gym Owner
```
POST /auth/login/owner
Content-Type: application/json

Request:
{
  "email": "owner@email.com",
  "password": "password"
}

Response (200):
{
  "success": true,
  "message": "Owner login successful",
  "data": {
    "ownerId": 1,
    "name": "Owner Name",
    "pan": "ABCDE1234F",
    "aadhaar": "123456789012",
    "gstin": "GSTIN123",
    "approved": true,
    "validated": true
  }
}
```

#### Register Customer
```
POST /auth/register/customer
Content-Type: application/json

Request:
{
  "fullName": "John Doe",
  "email": "customer@email.com",
  "password": "password123"
}

Response (200):
{
  "success": true,
  "message": "Customer registered successfully",
  "data": {
    "userId": 2,
    "fullName": "John Doe",
    "email": "customer@email.com"
  }
}
```

#### Register Owner
```
POST /auth/register/owner
Content-Type: application/json

Request:
{
  "fullName": "Owner Name",
  "email": "owner@email.com",
  "password": "password123",
  "pan": "ABCDE1234F",
  "aadhaar": "123456789012",
  "gstin": "GSTIN123"
}

Response (200):
{
  "success": true,
  "message": "Owner registered (pending approval)",
  "data": {
    "ownerId": 2,
    "name": "Owner Name",
    "approved": false,
    "validated": false
  }
}
```

---

### 2. ADMIN ENDPOINTS (`/admin`)

#### Validate Owner
```
POST /admin/owners/{ownerId}/validate

Response (200):
{
  "success": true,
  "message": "Owner validated",
  "data": null
}
```

#### Approve Owner
```
POST /admin/owners/{ownerId}/approve

Response (200):
{
  "success": true,
  "message": "Owner approved",
  "data": null
}
```

#### List All Owners
```
GET /admin/owners

Response (200):
{
  "success": true,
  "message": "Owners fetched",
  "data": [
    {
      "ownerId": 1,
      "name": "Owner 1",
      "approved": true,
      "validated": true
    }
  ]
}
```

#### Get Owner by ID
```
GET /admin/owners/{ownerId}

Response (200):
{
  "success": true,
  "message": "Owner fetched",
  "data": {
    "ownerId": 1,
    "name": "Owner 1",
    "pan": "ABCDE1234F",
    "approved": true
  }
}
```

#### Delete Owner
```
DELETE /admin/owners/{ownerId}

Response (200):
{
  "success": true,
  "message": "Owner deleted",
  "data": null
}
```

#### List All Customers
```
GET /admin/customers

Response (200):
{
  "success": true,
  "message": "Customers fetched",
  "data": [
    {
      "userId": 1,
      "fullName": "Customer 1",
      "email": "customer1@email.com"
    }
  ]
}
```

#### Get Customer by ID
```
GET /admin/customers/{customerId}

Response (200):
{
  "success": true,
  "message": "Customer fetched",
  "data": {
    "userId": 1,
    "fullName": "Customer 1",
    "email": "customer1@email.com"
  }
}
```

#### Add Gym Center
```
POST /admin/centers
Content-Type: application/json

Request:
{
  "gymName": "Cult Fit Bangalore",
  "city": "Bangalore",
  "state": "Karnataka",
  "pincode": 560001,
  "capacity": 100,
  "ownerId": 1
}

Response (200):
{
  "success": true,
  "message": "Gym center added",
  "data": {
    "centerId": 1,
    "gymName": "Cult Fit Bangalore",
    "ownerId": 1
  }
}
```

#### List All Centers
```
GET /admin/centers

Response (200):
{
  "success": true,
  "message": "Centers fetched",
  "data": [
    {
      "centerId": 1,
      "gymName": "Cult Fit",
      "city": "Bangalore"
    }
  ]
}
```

#### Add Slot to Center
```
POST /admin/centers/{centerId}/slots
Content-Type: application/json

Request:
{
  "startTime": "06:00",
  "endTime": "07:30",
  "seats": 20,
  "date": "2026-02-10"
}

Response (200):
{
  "success": true,
  "message": "Slot added",
  "data": {
    "slotId": 1,
    "centerId": 1,
    "startTime": "06:00",
    "endTime": "07:30",
    "seatsAvailable": 20
  }
}
```

#### List Slots for Center
```
GET /admin/centers/{centerId}/slots

Response (200):
{
  "success": true,
  "message": "Slots fetched",
  "data": [
    {
      "slotId": 1,
      "startTime": "06:00",
      "endTime": "07:30",
      "seatsAvailable": 20
    }
  ]
}
```

---

### 3. CUSTOMER ENDPOINTS (`/customers`)

#### Register Customer (alt)
```
POST /customers
Content-Type: application/json

Request:
{
  "fullName": "John Doe",
  "email": "john@email.com",
  "password": "pass123"
}

Response (200):
{
  "success": true,
  "message": "Customer registered",
  "data": { "userId": 1, "fullName": "John Doe" }
}
```

#### Get Customer Profile
```
GET /customers/{userId}

Response (200):
{
  "success": true,
  "message": "Customer fetched",
  "data": { "userId": 1, "fullName": "John Doe", "email": "john@email.com" }
}
```

#### Update Payment Details
```
POST /customers/{userId}/payment
Content-Type: application/json

Request:
{
  "paymentType": 1,
  "paymentInfo": "4111111111111111"
}

Response (200):
{
  "success": true,
  "message": "Payment details updated",
  "data": null
}
```

#### Get Payment Details
```
GET /customers/{userId}/payment

Response (200):
{
  "success": true,
  "message": "Payment details fetched",
  "data": { "userId": 1, "paymentType": 1, "paymentInfo": "**** **** **** 1111" }
}
```

---

### 4. BOOKING ENDPOINTS (`/bookings`)

#### Create Booking
```
POST /bookings
Content-Type: application/json

Request:
{
  "userId": 1,
  "slotId": 1,
  "centerId": 1
}

Response (200):
{
  "success": true,
  "message": "Booking created",
  "data": { "bookingId": 1, "userId": 1, "slotId": 1, "status": "CONFIRMED" }
}
```

#### Get User's Bookings
```
GET /bookings/user/{userId}

Response (200):
{
  "success": true,
  "message": "Bookings fetched",
  "data": [
    { "bookingId": 1, "userId": 1, "slotId": 1, "status": "CONFIRMED" }
  ]
}
```

#### Cancel Booking
```
DELETE /bookings/{bookingId}

Response (200):
{
  "success": true,
  "message": "Booking cancelled",
  "data": null
}
```

---

### 5. GYM CENTER ENDPOINTS (`/centers`)

#### List All Centers (Public)
```
GET /centers

Response (200):
{
  "success": true,
  "message": "Centers fetched",
  "data": [ { "centerId": 1, "gymName": "Cult Fit", "city": "Bangalore" } ]
}
```

#### Get Center Details
```
GET /centers/{centerId}

Response (200):
{
  "success": true,
  "message": "Center fetched",
  "data": { "centerId": 1, "gymName": "Cult Fit", "capacity": 100 }
}
```

#### List Center Slots
```
GET /centers/{centerId}/slots

Response (200):
{
  "success": true,
  "message": "Slots fetched",
  "data": [ { "slotId": 1, "startTime": "06:00", "seatsAvailable": 20 } ]
}
```

#### List Available Slots
```
GET /centers/{centerId}/slots/available?date=2026-02-10

Response (200):
{
  "success": true,
  "message": "Available slots fetched",
  "data": [ { "slotId": 1, "startTime": "06:00", "seatsAvailable": 20 } ]
}
```

---

### 6. GYM OWNER ENDPOINTS (`/owners`)

#### Get Owner Profile
```
GET /owners/{ownerId}

Response (200):
{
  "success": true,
  "message": "Owner fetched",
  "data": { "ownerId": 1, "name": "Owner Name", "approved": true }
}
```

#### List Owner's Centers
```
GET /owners/{ownerId}/centers

Response (200):
{
  "success": true,
  "message": "Owner centers fetched",
  "data": [ { "centerId": 1, "gymName": "Cult Fit", "ownerId": 1 } ]
}
```

#### Add Center (by Owner)
```
POST /owners/{ownerId}/centers
Content-Type: application/json

Request:
{
  "gymName": "New Gym",
  "city": "Delhi",
  "state": "Delhi",
  "pincode": 110001,
  "capacity": 50
}

Response (200):
{
  "success": true,
  "message": "Center added",
  "data": { "centerId": 2, "gymName": "New Gym", "ownerId": 1 }
}
```

#### Add Slot to Center (by Owner)
```
POST /owners/{ownerId}/centers/{centerId}/slots
Content-Type: application/json

Request:
{
  "startTime": "07:00",
  "endTime": "08:30",
  "seats": 15,
  "date": "2026-02-10"
}

Response (200):
{
  "success": true,
  "message": "Slot added",
  "data": { "slotId": 2, "centerId": 1, "startTime": "07:00", "seatsAvailable": 15 }
}
```

#### List Slots for Center (by Owner)
```
GET /owners/{ownerId}/centers/{centerId}/slots

Response (200):
{
  "success": true,
  "message": "Slots fetched",
  "data": [ { "slotId": 1, "startTime": "06:00", "seatsAvailable": 20 } ]
}
```

#### View Bookings for Center (by Owner)
```
GET /owners/{ownerId}/centers/{centerId}/bookings

Response (200):
{
  "success": true,
  "message": "Bookings fetched",
  "data": [
    { "bookingId": 1, "slotId": 1, "userId": 5, "status": "CONFIRMED" }
  ]
}
```

---

## DATABASE SCHEMA

Database name: **FlipFit**

Tables:
- `users` - Base user information (ADMIN, OWNER, CUSTOMER)
- `admins` - Admin-specific data
- `customers` - Customer-specific data (payment methods)
- `Owner` - Gym owner details (PAN, Aadhaar, GSTIN)
- `GymCentreTable` - Gym center information
- `slots` - Time slots for gyms
- `bookings` - Customer bookings
- `waitlist` - Waitlist for full slots

---

## ERROR RESPONSES

### Unauthorized (401)
```json
{
  "success": false,
  "message": "Invalid admin credentials",
  "data": null
}
```

### Not Found (404)
```json
{
  "success": false,
  "message": "Owner not found",
  "data": null
}
```

### Bad Request (400)
```json
{
  "success": false,
  "message": "Registration failed: Email already exists",
  "data": null
}
```

### Forbidden (403)
```json
{
  "success": false,
  "message": "Owner account pending approval",
  "data": null
}
```

---

## RUNNING THE APPLICATION

```bash
# Build
mvn clean package

# Run
java -jar target/flipfit-dropwizard-1.0.0.jar server config.yml

# Access
curl http://localhost:8080/admin/login
Admin interface: http://localhost:8081
```

---

## CURL EXAMPLES

### Login Admin
```bash
curl -X POST http://localhost:8080/auth/login/admin \
  -H "Content-Type: application/json" \
  -d '{"email":"admin@flipfit.com","password":"admin123"}'
```

### Register Customer
```bash
curl -X POST http://localhost:8080/auth/register/customer \
  -H "Content-Type: application/json" \
  -d '{"fullName":"John Doe","email":"john@example.com","password":"pass123"}'
```

### Create Booking
```bash
curl -X POST http://localhost:8080/bookings \
  -H "Content-Type: application/json" \
  -d '{"userId":1,"slotId":1,"centerId":1}'
```

### Get Available Slots
```bash
curl http://localhost:8080/centers/1/slots/available?date=2026-02-10
```

### Add Center (Owner)
```bash
curl -X POST http://localhost:8080/owners/1/centers \
  -H "Content-Type: application/json" \
  -d '{"gymName":"Anytime Fitness","city":"Mumbai","state":"Maharashtra","pincode":400001,"capacity":100}'
```
