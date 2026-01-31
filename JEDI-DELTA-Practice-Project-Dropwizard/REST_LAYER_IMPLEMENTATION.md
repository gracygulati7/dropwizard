# FlipFit Dropwizard REST Layer - Implementation Summary

## Overview
Successfully created a complete REST layer (`src/com/flipfit/rest`) with 4 REST controller classes that replicate all the functionality from the console-based client menu layer. Each controller has been designed to mirror the exact business logic from the corresponding client menu class.

## REST Controllers Created

### 1. **LoginController** (`src/com/flipfit/rest/LoginController.java`)
Maps to: `JEDI-DELTA-Practice-Project/src/com/flipfit/client/LoginMenu.java`

**Endpoints:**
- `POST /auth/login` - Generic login with role parameter (1=Owner, 2=Customer, 3=Admin)
- `POST /auth/login/owner` - Gym Owner login
- `POST /auth/login/customer` - Customer login
- `POST /auth/login/admin` - Admin login
- `POST /auth/register/owner` - Gym Owner registration
- `POST /auth/register/customer` - Customer registration

**Features:**
- Role-based authentication
- Approval verification for gym owners
- Email-based customer lookup
- Comprehensive error handling
- Generic `ApiResponse` wrapper for all responses

**Request DTOs:**
- `LoginRequest` - Contains email, password, role
- `OwnerRegistrationRequest` - Full name, email, password, PAN, Aadhaar, GSTIN
- `CustomerRegistrationRequest` - Full name, email, password, contact

---

### 2. **AdminController** (`src/com/flipfit/rest/AdminController.java`)
Maps to: `JEDI-DELTA-Practice-Project/src/com/flipfit/client/AdminMenu.java`

**Endpoints:**
- `GET /admin/owners` - View all gym owners
- `POST /admin/owners/{ownerId}/approve` - Approve gym owner
- `POST /admin/owners/{ownerId}/validate` - Validate gym owner
- `DELETE /admin/owners/{ownerId}` - Delete gym owner
- `GET /admin/customers` - View all customers
- `POST /admin/centers` - Add gym center (with auto-generated ID)
- `GET /admin/centers` - View all gym centers with slots
- `POST /admin/centers/{centerId}/slots` - Add slot to center (with auto-generated ID)
- `GET /admin/centers/{centerId}/slots` - View slots for a center
- `GET /admin/available-slots` - View available slots across all centers
- `GET /admin/profile/{adminId}` - View admin profile
- `PUT /admin/profile/{adminId}` - Edit admin profile

**Features:**
- Complete owner management (approve, validate, delete)
- Gym center creation with auto-increment support
- Slot management with auto-increment support
- Comprehensive slot viewing with filtering
- Service delegation pattern for business logic

**Request DTOs:**
- `AddGymCenterRequest` - Gym details with optional auto-ID support
- `AddSlotRequest` - Slot details with optional auto-ID support

---

### 3. **CustomerController** (`src/com/flipfit/rest/CustomerController.java`)
Maps to: `JEDI-DELTA-Practice-Project/src/com/flipfit/client/CustomerMenu.java`

**Endpoints:**
- `GET /customers/{userId}/gyms` - View available gyms
- `GET /customers/{userId}/bookings` - View customer's bookings
- `POST /customers/{userId}/book-slot` - Book a slot with full validation
- `DELETE /customers/{userId}/bookings/{bookingId}` - Cancel booking
- `GET /customers/{userId}/notifications` - View notifications
- `GET /customers/{userId}/available-slots` - View available slots
- `GET /customers/{userId}/profile` - View customer profile
- `PUT /customers/{userId}/profile` - Edit customer profile
- `GET /customers/{userId}/payment-info` - View payment information

**Features:**
- Complete booking workflow with waitlist support
- Date-based slot filtering
- Seat availability validation
- Payment processing integration
- Booking cancellation with user verification
- Comprehensive error handling for invalid selections
- Notifications management

**Request DTOs:**
- `BookSlotRequest` - Booking details with date, slot, center, payment amount
- `CancelBookingRequest` - Booking cancellation details

---

### 4. **GymOwnerController** (`src/com/flipfit/rest/GymOwnerController.java`)
Maps to: `JEDI-DELTA-Practice-Project/src/com/flipfit/client/GymOwnerMenu.java`

**Endpoints:**
- `POST /owners/{ownerId}/centers` - Add gym center (with auto-generated ID)
- `GET /owners/{ownerId}/centers` - View owner's centers
- `POST /owners/{ownerId}/centers/{centerId}/slots` - Add slot to center (with auto-generated ID)
- `GET /owners/{ownerId}/centers/{centerId}/slots` - View slots in center
- `GET /owners/{ownerId}/centers/{centerId}/customers` - View customers in center
- `GET /owners/{ownerId}/profile` - View owner profile
- `PUT /owners/{ownerId}/profile` - Edit owner profile

**Features:**
- Center management with auto-increment ID support
- Slot creation with validation (date format, time format, seat validation)
- Customer viewing for centers
- Profile management
- Business logic delegation to services

**Request DTOs:**
- `AddGymCentreRequest` - Center details
- `AddSlotRequest` - Slot details with date and time parsing

---

## Architecture Pattern

All REST controllers follow a consistent pattern:

```
REST Controller
    ↓ (receives HTTP request via @Path annotations)
    ↓ (validates request DTOs)
    ↓ (delegates to Business Services/DAOs)
    ↓ (formats response using ApiResponse wrapper)
    ↓ (returns HTTP Response with appropriate status code)
```

### Response Format
All endpoints return a standardized `ApiResponse` wrapper:
```json
{
  "success": true/false,
  "message": "Human-readable message",
  "data": { /* Optional response data */ }
}
```

### HTTP Status Codes
- `200 OK` - Success (GET operations, successful operations)
- `201 Created` - Resource created (POST operations)
- `400 Bad Request` - Invalid input
- `401 Unauthorized` - Authentication failed
- `403 Forbidden` - Access denied
- `404 Not Found` - Resource not found
- `500 Internal Server Error` - Server error

---

## Key Features Implemented

### 1. **Auto-Increment ID Support**
- Centers: Uses `GymCentreDAO.getNextCentreId()`
- Slots: Uses `SlotDAO.getNextSlotId()`
- DTOs allow optional IDs for backward compatibility

### 2. **Date/Time Handling**
- Booking dates: YYYY-MM-DD format using `LocalDate.parse()`
- Time format: HH:MM 24-hour format with validation
- Date filtering for available slots

### 3. **Validation**
- Required field validation for all DTOs
- Date/time format validation
- Slot availability checks
- Seat capacity validation
- Booking ownership verification

### 4. **Business Logic Integration**
- Direct delegation to existing business services
- DAO instance singletons via `getInstance()`
- Full service layer utilization (BookingService, UserService, etc.)

### 5. **Error Handling**
- Comprehensive try-catch blocks
- Specific error messages for debugging
- Appropriate HTTP status codes
- Validation error messages

---

## Integration with Main Application

The `FlipFitDropwizardApplication` class has been updated to register all 4 REST controllers:

```java
environment.jersey().register(new LoginController());
environment.jersey().register(new AdminController());
environment.jersey().register(new CustomerController());
environment.jersey().register(new GymOwnerController());
```

---

## Path Mappings

### Authentication Routes
- `POST /auth/login` - Generic login
- `POST /auth/login/owner` - Owner login
- `POST /auth/login/customer` - Customer login
- `POST /auth/login/admin` - Admin login
- `POST /auth/register/owner` - Owner registration
- `POST /auth/register/customer` - Customer registration

### Admin Routes
- `GET /admin/owners` - List owners
- `POST /admin/owners/{ownerId}/approve` - Approve owner
- `POST /admin/owners/{ownerId}/validate` - Validate owner
- `DELETE /admin/owners/{ownerId}` - Delete owner
- `GET /admin/customers` - List customers
- `POST /admin/centers` - Add center
- `GET /admin/centers` - List centers
- `POST /admin/centers/{centerId}/slots` - Add slot
- `GET /admin/centers/{centerId}/slots` - List slots
- `GET /admin/available-slots` - List available slots
- `GET /admin/profile/{adminId}` - View profile
- `PUT /admin/profile/{adminId}` - Edit profile

### Customer Routes
- `GET /customers/{userId}/gyms` - View gyms
- `GET /customers/{userId}/bookings` - View bookings
- `POST /customers/{userId}/book-slot` - Book slot
- `DELETE /customers/{userId}/bookings/{bookingId}` - Cancel booking
- `GET /customers/{userId}/notifications` - View notifications
- `GET /customers/{userId}/available-slots` - View available slots
- `GET /customers/{userId}/profile` - View profile
- `PUT /customers/{userId}/profile` - Edit profile
- `GET /customers/{userId}/payment-info` - View payment info

### Gym Owner Routes
- `POST /owners/{ownerId}/centers` - Add center
- `GET /owners/{ownerId}/centers` - View centers
- `POST /owners/{ownerId}/centers/{centerId}/slots` - Add slot
- `GET /owners/{ownerId}/centers/{centerId}/slots` - View slots
- `GET /owners/{ownerId}/centers/{centerId}/customers` - View customers
- `GET /owners/{ownerId}/profile` - View profile
- `PUT /owners/{ownerId}/profile` - Edit profile

---

## Files Modified/Created

### Created Files:
1. ✅ `src/com/flipfit/rest/LoginController.java` - Authentication controller
2. ✅ `src/com/flipfit/rest/AdminController.java` - Admin operations controller
3. ✅ `src/com/flipfit/rest/CustomerController.java` - Customer operations controller
4. ✅ `src/com/flipfit/rest/GymOwnerController.java` - Gym owner operations controller

### Modified Files:
1. ✅ `src/com/flipfit/api/FlipFitDropwizardApplication.java` - Updated to register new controllers

---

## Testing Recommendations

### Sample API Calls:

**Admin Login:**
```bash
curl -X POST http://localhost:8080/auth/login/admin \
  -H "Content-Type: application/json" \
  -d '{
    "email": "admin@flipfit.com",
    "password": "admin123"
  }'
```

**Customer Registration:**
```bash
curl -X POST http://localhost:8080/auth/register/customer \
  -H "Content-Type: application/json" \
  -d '{
    "fullName": "John Doe",
    "email": "john@example.com",
    "password": "pass123",
    "contact": "9999999999"
  }'
```

**Book a Slot:**
```bash
curl -X POST http://localhost:8080/customers/1/book-slot \
  -H "Content-Type: application/json" \
  -d '{
    "userId": 1,
    "centerId": 1,
    "slotId": 5,
    "bookingDate": "2026-02-15",
    "paymentAmount": 500
  }'
```

**Add Gym Center (as Owner):**
```bash
curl -X POST http://localhost:8080/owners/2/centers \
  -H "Content-Type: application/json" \
  -d '{
    "gymName": "FitZone Premium",
    "city": "Bangalore",
    "state": "Karnataka",
    "pincode": 560001,
    "capacity": 100
  }'
```

---

## Status

✅ **COMPLETE** - All REST controllers created and integrated with main application

The REST layer now provides HTTP-based access to all functionality previously available only through console menus. All business logic is preserved and reused from the existing service and DAO layers.
