package com.flipfit.rest;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import com.flipfit.bean.FlipFitGymOwner;
import com.flipfit.bean.FlipFitCustomer;
import com.flipfit.business.GymOwnerService;
import com.flipfit.business.GymOwnerServiceImpl;
import com.flipfit.dao.OwnerDAO;
import com.flipfit.dao.CustomerDAO;
import com.flipfit.dao.AdminDAO;

/**
 * REST Controller for authentication (login and registration)
 * Maps to LoginMenu functionality
 */
@Path("/auth")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class LoginController {
    
    private GymOwnerService gymOwnerService = new GymOwnerServiceImpl();
    
    /**
     * Generic API Response wrapper
     */
    public static class ApiResponse {
        public boolean success;
        public String message;
        public Object data;
        
        public ApiResponse(boolean success, String message, Object data) {
            this.success = success;
            this.message = message;
            this.data = data;
        }
        
        public static ApiResponse ok(String message, Object data) {
            return new ApiResponse(true, message, data);
        }
        
        public static ApiResponse fail(String message) {
            return new ApiResponse(false, message, null);
        }
    }
    
    /**
     * Request DTOs
     */
    public static class LoginRequest {
        public String email;
        public String password;
        public int role; // 1=Owner, 2=Customer, 3=Admin
    }
    
    public static class OwnerRegistrationRequest {
        public String fullName;
        public String email;
        public String password;
        public String pan;
        public String aadhaar;
        public String gstin;
    }
    
    public static class CustomerRegistrationRequest {
        public String fullName;
        public String email;
        public String password;
        public String contact;
    }
    
    /**
     * POST /auth/login - Generic login endpoint
     * Accepts role parameter (1=Owner, 2=Customer, 3=Admin)
     */
    @POST
    @Path("/login")
    public Response login(LoginRequest request) {
        if (request.email == null || request.password == null) {
            return Response.status(400)
                    .entity(ApiResponse.fail("Email and password are required"))
                    .build();
        }
        
        switch (request.role) {
            case 1:
                return loginOwner(request.email, request.password);
            case 2:
                return loginCustomer(request.email, request.password);
            case 3:
                return loginAdmin(request.email, request.password);
            default:
                return Response.status(400)
                        .entity(ApiResponse.fail("Invalid role. Use 1=Owner, 2=Customer, 3=Admin"))
                        .build();
        }
    }
    
    /**
     * POST /auth/login/owner - Gym Owner login
     */
    @POST
    @Path("/login/owner")
    public Response loginOwner(LoginRequest request) {
        return loginOwner(request.email, request.password);
    }
    
    private Response loginOwner(String email, String password) {
        OwnerDAO ownerDAO = OwnerDAO.getInstance();
        FlipFitGymOwner owner = ownerDAO.getOwnerByEmail(email);
        
        if (owner == null) {
            return Response.status(401)
                    .entity(ApiResponse.fail("Gym Owner account not found for email: " + email))
                    .build();
        }
        
        if (!owner.isApproved()) {
            return Response.status(403)
                    .entity(ApiResponse.fail("Your account is still pending admin approval"))
                    .build();
        }
        
        return Response.ok(ApiResponse.ok("Logged in as Gym Owner: " + owner.getName(), owner)).build();
    }
    
    /**
     * POST /auth/login/customer - Customer login
     */
    @POST
    @Path("/login/customer")
    public Response loginCustomer(LoginRequest request) {
        return loginCustomer(request.email, request.password);
    }
    
    private Response loginCustomer(String email, String password) {
        CustomerDAO customerDAO = CustomerDAO.getInstance();
        FlipFitCustomer customer = customerDAO.getCustomerByEmail(email);
        
        if (customer == null) {
            return Response.status(401)
                    .entity(ApiResponse.fail("Customer account not found. Please register"))
                    .build();
        }
        
        return Response.ok(ApiResponse.ok("Logged in as Gym Customer: " + customer.getFullName(), customer)).build();
    }
    
    /**
     * POST /auth/login/admin - Admin login
     */
    @POST
    @Path("/login/admin")
    public Response loginAdmin(LoginRequest request) {
        return loginAdmin(request.email, request.password);
    }
    
    private Response loginAdmin(String email, String password) {
        AdminDAO adminDAO = AdminDAO.getInstance();
        
        if (!adminDAO.login(email, password)) {
            return Response.status(401)
                    .entity(ApiResponse.fail("Invalid admin credentials"))
                    .build();
        }
        
        return Response.ok(ApiResponse.ok("Logged in as Gym Admin", null)).build();
    }
    
    /**
     * POST /auth/register/owner - Gym Owner registration
     */
    @POST
    @Path("/register/owner")
    public Response registerOwner(OwnerRegistrationRequest request) {
        if (request.fullName == null || request.email == null || request.password == null ||
            request.pan == null || request.aadhaar == null || request.gstin == null) {
            return Response.status(400)
                    .entity(ApiResponse.fail("All fields are required for owner registration"))
                    .build();
        }
        
        try {
            gymOwnerService.registerOwner(request.fullName, request.email, request.password, 
                                         request.pan, request.aadhaar, request.gstin);
            return Response.status(201)
                    .entity(ApiResponse.ok("Gym Owner Registration successful! Your account is pending admin approval", null))
                    .build();
        } catch (Exception e) {
            return Response.status(500)
                    .entity(ApiResponse.fail("Registration failed: " + e.getMessage()))
                    .build();
        }
    }
    
    /**
     * POST /auth/register/customer - Customer registration
     */
    @POST
    @Path("/register/customer")
    public Response registerCustomer(CustomerRegistrationRequest request) {
        if (request.fullName == null || request.email == null || request.password == null) {
            return Response.status(400)
                    .entity(ApiResponse.fail("Full name, email, and password are required"))
                    .build();
        }
        
        try {
            CustomerDAO customerDAO = CustomerDAO.getInstance();
            FlipFitCustomer customer = customerDAO.addCustomer(request.fullName, request.email, request.password);
            
            if (request.contact != null) {
                customer.setContact(request.contact);
                customerDAO.updateCustomer(customer);
            }
            
            return Response.status(201)
                    .entity(ApiResponse.ok("Customer registration successful! You can now login with email: " + request.email, customer))
                    .build();
        } catch (Exception e) {
            return Response.status(500)
                    .entity(ApiResponse.fail("Registration failed: " + e.getMessage()))
                    .build();
        }
    }
}
