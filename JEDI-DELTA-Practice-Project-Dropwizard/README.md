# FlipFit Dropwizard REST API

## Structure (new REST layer)
```
JEDI-DELTA-Practice-Project-Dropwizard/
  pom.xml
  config.yml
  src/
    com/flipfit/api/
      FlipFitDropwizardApplication.java
      FlipFitConfiguration.java
      health/FlipFitHealthCheck.java
      resources/
        AdminResource.java
        BookingResource.java
        CenterResource.java
        CustomerResource.java
        GymOwnerResource.java
      dto/
        AdminLoginRequest.java
        AddGymCenterRequest.java
        AddSlotRequest.java
        ApiResponse.java
        CreateBookingRequest.java
        PaymentRequest.java
        RegisterCustomerRequest.java
        RegisterOwnerRequest.java
```

## Plan
1. Add Dropwizard bootstrap (`pom.xml`, `config.yml`, `Application`, `Configuration`).
2. Implement REST resources for admin, bookings, centers, customers, and gym owners.
3. Add DTOs for request/response payloads and basic validation.
4. Wire resources in the application and provide a health check.

## Notes
- Existing services/DAOs are reused from the original codebase under `src/com/flipfit`.
- DB settings currently come from `com.flipfit.util.DBUtil` (hardcoded). You can later refactor to use Dropwizard `DataSourceFactory`.
