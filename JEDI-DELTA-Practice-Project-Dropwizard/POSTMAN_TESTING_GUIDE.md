# Postman Testing Guide - FlipFit REST APIs

## Prerequisites

- ✅ Postman installed (download from https://www.postman.com/downloads/)
- ✅ Application running: `java -jar target/flipfit-dropwizard-1.0.0-SNAPSHOT.jar server config.yml`
- ✅ MySQL running with FlipFit database
- ✅ Base URL: `http://localhost:8080`

---

## Step 1: Setup Postman Environment Variables

### Create Environment

1. Click **Environments** (bottom left)
2. Click **+** to create new environment
3. **Environment name**: `FlipFit Local`

### Add Variables

| Variable | Type | Value |
|----------|------|-------|
| `baseUrl` | string | `http://localhost:8080` |
| `adminPort` | string | `8081` |
| `token` | string | (leave empty, will populate during tests) |
| `customerId` | string | `1` |
| `ownerId` | string | `1` |
| `centerId` | string | `1` |
| `slotId` | string | `1` |
| `bookingId` | string | `1` |

### Save Environment

Click **Save** → Select this environment (dropdown top right)

---

## Step 2: Create Postman Collection

### Method 1: Import from JSON

Copy the collection JSON below and import:

1. Click **Collections** (left side)
2. Click **Import**
3. Paste the collection JSON
4. Click **Import**

### Method 2: Create Manually

1. Click **Collections** → **+** (New Collection)
2. Name: `FlipFit Dropwizard APIs`
3. Add folders for each controller

---

## Authentication Endpoints

### 1. Admin Login

**POST** `{{baseUrl}}/auth/login/admin`

**Headers**:
```
Content-Type: application/json
```

**Body** (raw JSON):
```json
{
  "email": "admin@flipfit.com",
  "password": "admin123"
}
```

**Expected Response (200 OK)**:
```json
{
  "success": true,
  "message": "Logged in as Gym Admin",
  "data": null
}
```

---

### 2. Customer Login

**POST** `{{baseUrl}}/auth/login/customer`

**Headers**:
```
Content-Type: application/json
```

**Body** (raw JSON):
```json
{
  "email": "customer@example.com",
  "password": "pass123"
}
```

**Expected Response (200 OK)**:
```json
{
  "success": true,
  "message": "Logged in as Gym Customer: Customer Name",
  "data": {
    "userId": 1,
    "fullName": "Customer Name",
    "email": "customer@example.com",
    "contact": "9999999999"
  }
}
```

---

### 3. Gym Owner Login

**POST** `{{baseUrl}}/auth/login/owner`

**Headers**:
```
Content-Type: application/json
```

**Body** (raw JSON):
```json
{
  "email": "owner@gym.com",
  "password": "pass123"
}
```

**Expected Response (200 OK)**:
```json
{
  "success": true,
  "message": "Logged in as Gym Owner: Owner Name",
  "data": {
    "ownerId": 1,
    "name": "Owner Name",
    "email": "owner@gym.com",
    "approved": true
  }
}
```

---

### 4. Register Customer

**POST** `{{baseUrl}}/auth/register/customer`

**Headers**:
```
Content-Type: application/json
```

**Body** (raw JSON):
```json
{
  "fullName": "New Customer",
  "email": "newcustomer@example.com",
  "password": "pass123",
  "contact": "8888888888"
}
```

**Expected Response (201 Created)**:
```json
{
  "success": true,
  "message": "Customer registration successful! You can now login with email: newcustomer@example.com",
  "data": {
    "userId": 2,
    "fullName": "New Customer",
    "email": "newcustomer@example.com",
    "contact": "8888888888"
  }
}
```

---

### 5. Register Gym Owner

**POST** `{{baseUrl}}/auth/register/owner`

**Headers**:
```
Content-Type: application/json
```

**Body** (raw JSON):
```json
{
  "fullName": "New Gym Owner",
  "email": "newowner@gym.com",
  "password": "pass123",
  "pan": "ABCDE1234F",
  "aadhaar": "123456789012",
  "gstin": "GSTIN123456"
}
```

**Expected Response (201 Created)**:
```json
{
  "success": true,
  "message": "Gym Owner Registration successful! Your account is pending admin approval",
  "data": null
}
```

---

## Admin Endpoints

### 6. List All Gym Owners

**GET** `{{baseUrl}}/admin/owners`

**Headers**:
```
Content-Type: application/json
```

**Expected Response (200 OK)**:
```json
{
  "success": true,
  "message": "All gym owners fetched",
  "data": []
}
```

---

### 7. Approve Gym Owner

**POST** `{{baseUrl}}/admin/owners/1/approve`

**Headers**:
```
Content-Type: application/json
```

**Expected Response (200 OK)**:
```json
{
  "success": true,
  "message": "Gym owner 1 approved successfully",
  "data": null
}
```

---

### 8. Validate Gym Owner

**POST** `{{baseUrl}}/admin/owners/1/validate`

**Headers**:
```
Content-Type: application/json
```

**Expected Response (200 OK)**:
```json
{
  "success": true,
  "message": "Gym owner 1 validated successfully",
  "data": null
}
```

---

### 9. Delete Gym Owner

**DELETE** `{{baseUrl}}/admin/owners/1`

**Expected Response (200 OK)**:
```json
{
  "success": true,
  "message": "Gym owner 1 deleted successfully",
  "data": null
}
```

---

### 10. List All Customers

**GET** `{{baseUrl}}/admin/customers`

**Expected Response (200 OK)**:
```json
{
  "success": true,
  "message": "All customers fetched",
  "data": []
}
```

---

### 11. Add Gym Center

**POST** `{{baseUrl}}/admin/centers`

**Headers**:
```
Content-Type: application/json
```

**Body** (raw JSON):
```json
{
  "gymName": "FitZone Premium",
  "city": "Bangalore",
  "state": "Karnataka",
  "pincode": 560001,
  "capacity": 100
}
```

**Expected Response (201 Created)**:
```json
{
  "success": true,
  "message": "Gym center added successfully with ID: 1",
  "data": null
}
```

---

### 12. List All Gym Centers

**GET** `{{baseUrl}}/admin/centers`

**Expected Response (200 OK)**:
```json
{
  "success": true,
  "message": "Gym centers fetched successfully",
  "data": [
    {
      "gymId": 1,
      "gymName": "FitZone Premium",
      "location": "Bangalore, Karnataka",
      "capacity": 100
    }
  ]
}
```

---

### 13. Add Slot to Center

**POST** `{{baseUrl}}/admin/centers/1/slots`

**Headers**:
```
Content-Type: application/json
```

**Body** (raw JSON):
```json
{
  "startTime": "06:00",
  "endTime": "07:30",
  "seats": 20
}
```

**Expected Response (201 Created)**:
```json
{
  "success": true,
  "message": "Slot added successfully with ID: 1",
  "data": null
}
```

---

### 14. List Slots for Center

**GET** `{{baseUrl}}/admin/centers/1/slots`

**Expected Response (200 OK)**:
```json
{
  "success": true,
  "message": "Slots for center 1 fetched",
  "data": []
}
```

---

### 15. View Available Slots

**GET** `{{baseUrl}}/admin/available-slots`

**Expected Response (200 OK)**:
```json
{
  "success": true,
  "message": "Available slots fetched",
  "data": []
}
```

---

### 16. View Admin Profile

**GET** `{{baseUrl}}/admin/profile/1`

**Expected Response (200 OK)**:
```json
{
  "success": true,
  "message": "Admin profile retrieved",
  "data": null
}
```

---

### 17. Edit Admin Profile

**PUT** `{{baseUrl}}/admin/profile/1`

**Expected Response (200 OK)**:
```json
{
  "success": true,
  "message": "Admin profile updated",
  "data": null
}
```

---

## Customer Endpoints

### 18. View Available Gyms

**GET** `{{baseUrl}}/customers/1/gyms`

**Expected Response (200 OK)**:
```json
{
  "success": true,
  "message": "Available gyms fetched",
  "data": [
    {
      "gymId": 1,
      "gymName": "FitZone Premium",
      "location": "Bangalore, Karnataka"
    }
  ]
}
```

---

### 19. View Customer Bookings

**GET** `{{baseUrl}}/customers/1/bookings`

**Expected Response (200 OK)**:
```json
{
  "success": true,
  "message": "Customer bookings fetched",
  "data": []
}
```

---

### 20. Book a Slot

**POST** `{{baseUrl}}/customers/1/book-slot`

**Headers**:
```
Content-Type: application/json
```

**Body** (raw JSON):
```json
{
  "userId": 1,
  "centerId": 1,
  "slotId": 1,
  "bookingDate": "2026-02-15",
  "paymentAmount": 500
}
```

**Expected Response (201 Created)**:
```json
{
  "success": true,
  "message": "Booking confirmed",
  "data": {
    "bookingId": 1,
    "userId": 1,
    "slotId": 1,
    "centerId": 1,
    "slotDate": "2026-02-15",
    "startTime": "06:00",
    "endTime": "07:30"
  }
}
```

---

### 21. Cancel Booking

**DELETE** `{{baseUrl}}/customers/1/bookings/1`

**Expected Response (200 OK)**:
```json
{
  "success": true,
  "message": "Booking cancelled successfully",
  "data": null
}
```

---

### 22. View Notifications

**GET** `{{baseUrl}}/customers/1/notifications`

**Expected Response (200 OK)**:
```json
{
  "success": true,
  "message": "Notifications retrieved",
  "data": null
}
```

---

### 23. View Available Slots

**GET** `{{baseUrl}}/customers/1/available-slots`

**Expected Response (200 OK)**:
```json
{
  "success": true,
  "message": "Available slots fetched",
  "data": []
}
```

---

### 24. View Customer Profile

**GET** `{{baseUrl}}/customers/1/profile`

**Expected Response (200 OK)**:
```json
{
  "success": true,
  "message": "Profile retrieved",
  "data": null
}
```

---

### 25. Edit Customer Profile

**PUT** `{{baseUrl}}/customers/1/profile`

**Expected Response (200 OK)**:
```json
{
  "success": true,
  "message": "Profile updated successfully",
  "data": null
}
```

---

### 26. View Payment Info

**GET** `{{baseUrl}}/customers/1/payment-info`

**Expected Response (200 OK)**:
```json
{
  "success": true,
  "message": "Payment info retrieved",
  "data": null
}
```

---

## Gym Owner Endpoints

### 27. Add Gym Center (Owner)

**POST** `{{baseUrl}}/owners/1/centers`

**Headers**:
```
Content-Type: application/json
```

**Body** (raw JSON):
```json
{
  "gymName": "My Fitness Center",
  "city": "Mumbai",
  "state": "Maharashtra",
  "pincode": 400001,
  "capacity": 150
}
```

**Expected Response (201 Created)**:
```json
{
  "success": true,
  "message": "Gym center added successfully with ID: 2",
  "data": null
}
```

---

### 28. View Owner's Centers

**GET** `{{baseUrl}}/owners/1/centers`

**Expected Response (200 OK)**:
```json
{
  "success": true,
  "message": "Owner centers fetched",
  "data": [
    {
      "gymId": 1,
      "gymName": "My Fitness Center",
      "location": "Mumbai, Maharashtra"
    }
  ]
}
```

---

### 29. Add Slot to Owner's Center

**POST** `{{baseUrl}}/owners/1/centers/1/slots`

**Headers**:
```
Content-Type: application/json
```

**Body** (raw JSON):
```json
{
  "centerId": 1,
  "date": "2026-02-20",
  "startTime": "07:00",
  "endTime": "08:30",
  "seats": 25
}
```

**Expected Response (201 Created)**:
```json
{
  "success": true,
  "message": "Slot added successfully with ID: 2",
  "data": {
    "slotId": 2,
    "centerId": 1,
    "startTime": "07:00",
    "endTime": "08:30",
    "totalSeats": 25,
    "seatsAvailable": 25,
    "date": "2026-02-20"
  }
}
```

---

### 30. View Slots in Owner's Center

**GET** `{{baseUrl}}/owners/1/centers/1/slots`

**Expected Response (200 OK)**:
```json
{
  "success": true,
  "message": "Slots for center 1 fetched",
  "data": [
    {
      "slotId": 1,
      "centerId": 1,
      "startTime": "06:00",
      "endTime": "07:30",
      "totalSeats": 20,
      "seatsAvailable": 20,
      "date": "2026-02-15"
    }
  ]
}
```

---

### 31. View Customers in Owner's Center

**GET** `{{baseUrl}}/owners/1/centers/1/customers`

**Expected Response (200 OK)**:
```json
{
  "success": true,
  "message": "Customers for center 1 fetched",
  "data": [
    {
      "bookingId": 1,
      "userId": 1,
      "slotId": 1,
      "centerId": 1,
      "bookingStatus": "CONFIRMED"
    }
  ]
}
```

---

### 32. View Owner Profile

**GET** `{{baseUrl}}/owners/1/profile`

**Expected Response (200 OK)**:
```json
{
  "success": true,
  "message": "Owner profile retrieved",
  "data": null
}
```

---

### 33. Edit Owner Profile

**PUT** `{{baseUrl}}/owners/1/profile`

**Expected Response (200 OK)**:
```json
{
  "success": true,
  "message": "Owner profile updated successfully",
  "data": null
}
```

---

## Health Check Endpoint

### 34. Health Check (Admin Port)

**GET** `http://localhost:8081/healthcheck`

**Expected Response (200 OK)**:
```json
{
  "deadline": "2226-01-31T10:00:00.000Z",
  "isHealthy": true
}
```

---

## Testing Workflow

### Workflow 1: Complete User Registration & Booking

1. **Register as Customer**
   - POST `/auth/register/customer`
   - Note the customer ID

2. **Login as Customer**
   - POST `/auth/login/customer`
   - Verify login successful

3. **View Available Gyms**
   - GET `/customers/{customerId}/gyms`
   - Note gym ID

4. **View Available Slots**
   - GET `/customers/{customerId}/available-slots`
   - Note slot ID

5. **Book a Slot**
   - POST `/customers/{customerId}/book-slot`
   - Provide centerId, slotId, bookingDate, paymentAmount

6. **View Your Bookings**
   - GET `/customers/{customerId}/bookings`
   - See your new booking

---

### Workflow 2: Admin Managing Owners & Centers

1. **Login as Admin**
   - POST `/auth/login/admin`

2. **View All Owners**
   - GET `/admin/owners`

3. **Register New Owner** (as customer first to get ID)
   - POST `/auth/register/owner`

4. **Approve Owner**
   - POST `/admin/owners/{ownerId}/approve`

5. **Add Gym Center**
   - POST `/admin/centers`

6. **Add Slot to Center**
   - POST `/admin/centers/{centerId}/slots`

7. **View All Centers**
   - GET `/admin/centers`

---

### Workflow 3: Gym Owner Managing Centers

1. **Register as Owner**
   - POST `/auth/register/owner`

2. **Wait for Admin Approval**
   - Admin: POST `/admin/owners/{ownerId}/approve`

3. **Login as Owner**
   - POST `/auth/login/owner`

4. **Add Gym Center**
   - POST `/owners/{ownerId}/centers`

5. **Add Slot to Center**
   - POST `/owners/{ownerId}/centers/{centerId}/slots`

6. **View Your Centers**
   - GET `/owners/{ownerId}/centers`

7. **View Customers in Your Center**
   - GET `/owners/{ownerId}/centers/{centerId}/customers`

---

## Error Responses

### 400 Bad Request
```json
{
  "success": false,
  "message": "Email and password are required",
  "data": null
}
```

### 401 Unauthorized
```json
{
  "success": false,
  "message": "Invalid admin credentials",
  "data": null
}
```

### 403 Forbidden
```json
{
  "success": false,
  "message": "Your account is still pending admin approval",
  "data": null
}
```

### 404 Not Found
```json
{
  "success": false,
  "message": "Gym Owner account not found for email: test@example.com",
  "data": null
}
```

### 500 Internal Server Error
```json
{
  "success": false,
  "message": "Registration failed: Database error",
  "data": null
}
```

---

## Postman Tips & Tricks

### Save Response as Variable

In **Tests** tab:
```javascript
pm.environment.set("customerId", pm.response.json().data.userId);
```

### Use Variables in Requests

Path: `{{baseUrl}}/customers/{{customerId}}/bookings`

### Pre-request Script (Auto-generate Timestamp)

```javascript
pm.environment.set("timestamp", new Date().toISOString());
```

### Check Response Status

In **Tests** tab:
```javascript
pm.test("Status code is 200", function () {
    pm.response.to.have.status(200);
});
```

### Validate Response Structure

```javascript
pm.test("Response has success field", function () {
    pm.expect(pm.response.json()).to.have.property("success");
});
```

---

## Export/Share Collection

1. Right-click collection
2. **Export**
3. Choose format (JSON recommended)
4. Save file
5. Share with team

---

## Common Test Scenarios

| Scenario | Steps |
|----------|-------|
| **Customer Books a Slot** | Register → Login → Browse Gyms → Book Slot → View Bookings |
| **Admin Manages Owners** | Login → View Owners → Approve Owner → Delete Owner |
| **Owner Creates Center** | Register → Wait Approval → Login → Add Center → Add Slot |
| **Slot Availability** | Check Slots → Book → Check Updated Availability |
| **Error Handling** | Try invalid email → Verify error response |

---

## Quick Reference - All 34 Endpoints

**Authentication (6)**
- POST /auth/login
- POST /auth/login/owner
- POST /auth/login/customer
- POST /auth/login/admin
- POST /auth/register/owner
- POST /auth/register/customer

**Admin (12)**
- GET /admin/owners
- POST /admin/owners/{id}/approve
- POST /admin/owners/{id}/validate
- DELETE /admin/owners/{id}
- GET /admin/customers
- POST /admin/centers
- GET /admin/centers
- POST /admin/centers/{id}/slots
- GET /admin/centers/{id}/slots
- GET /admin/available-slots
- GET /admin/profile/{id}
- PUT /admin/profile/{id}

**Customer (9)**
- GET /customers/{id}/gyms
- GET /customers/{id}/bookings
- POST /customers/{id}/book-slot
- DELETE /customers/{id}/bookings/{id}
- GET /customers/{id}/notifications
- GET /customers/{id}/available-slots
- GET /customers/{id}/profile
- PUT /customers/{id}/profile
- GET /customers/{id}/payment-info

**Gym Owner (7)**
- POST /owners/{id}/centers
- GET /owners/{id}/centers
- POST /owners/{id}/centers/{id}/slots
- GET /owners/{id}/centers/{id}/slots
- GET /owners/{id}/centers/{id}/customers
- GET /owners/{id}/profile
- PUT /owners/{id}/profile

---

**Ready to test all 34 endpoints in Postman!** 🚀
