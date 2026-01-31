package com.flipfit.api.resources;

import com.flipfit.api.dto.AdminLoginRequest;
import com.flipfit.api.dto.ApiResponse;
import com.flipfit.api.dto.RegisterCustomerRequest;
import com.flipfit.api.dto.RegisterOwnerRequest;
import com.flipfit.bean.FlipFitCustomer;
import com.flipfit.bean.FlipFitGymOwner;
import com.flipfit.dao.AdminDAO;
import com.flipfit.dao.CustomerDAO;
import com.flipfit.dao.OwnerDAO;

import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/auth")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AuthResource {

    private final AdminDAO adminDAO = AdminDAO.getInstance();
    private final CustomerDAO customerDAO = CustomerDAO.getInstance();
    private final OwnerDAO ownerDAO = OwnerDAO.getInstance();

    @POST
    @Path("/login/admin")
    public Response loginAdmin(@Valid AdminLoginRequest request) {
        boolean ok = adminDAO.login(request.getEmail(), request.getPassword());
        if (!ok) {
            return Response.status(Response.Status.UNAUTHORIZED)
                    .entity(ApiResponse.fail("Invalid admin credentials"))
                    .build();
        }
        return Response.ok(ApiResponse.ok("Admin login successful", "token-admin")).build();
    }

    @POST
    @Path("/login/customer")
    public Response loginCustomer(@Valid AdminLoginRequest request) {
        FlipFitCustomer customer = customerDAO.getCustomerByEmail(request.getEmail());
        if (customer == null) {
            return Response.status(Response.Status.UNAUTHORIZED)
                    .entity(ApiResponse.fail("Invalid customer email"))
                    .build();
        }
        return Response.ok(ApiResponse.ok("Customer login successful", customer)).build();
    }

    @POST
    @Path("/login/owner")
    public Response loginOwner(@Valid AdminLoginRequest request) {
        FlipFitGymOwner owner = ownerDAO.getOwnerByEmail(request.getEmail());
        if (owner == null) {
            return Response.status(Response.Status.UNAUTHORIZED)
                    .entity(ApiResponse.fail("Invalid owner email"))
                    .build();
        }
        if (!owner.isApproved()) {
            return Response.status(Response.Status.FORBIDDEN)
                    .entity(ApiResponse.fail("Owner account pending approval"))
                    .build();
        }
        return Response.ok(ApiResponse.ok("Owner login successful", owner)).build();
    }

    @POST
    @Path("/register/customer")
    public Response registerCustomer(@Valid RegisterCustomerRequest request) {
        try {
            FlipFitCustomer customer = customerDAO.addCustomer(request.getFullName(), request.getEmail(), request.getPassword());
            return Response.ok(ApiResponse.ok("Customer registered successfully", customer)).build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(ApiResponse.fail("Registration failed: " + e.getMessage()))
                    .build();
        }
    }

    @POST
    @Path("/register/owner")
    public Response registerOwner(@Valid RegisterOwnerRequest request) {
        try {
            ownerDAO.addOwner(request.getFullName(), request.getEmail(), request.getPassword());
            int ownerId = ownerDAO.getUserIdByEmail(request.getEmail());
            if (ownerId != -1) {
                ownerDAO.addOwnerDetails(ownerId, request.getPan(), request.getAadhaar(), request.getGstin());
                FlipFitGymOwner owner = ownerDAO.getOwnerById(ownerId);
                return Response.ok(ApiResponse.ok("Owner registered (pending approval)", owner)).build();
            }
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(ApiResponse.fail("Registration failed"))
                    .build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(ApiResponse.fail("Registration failed: " + e.getMessage()))
                    .build();
        }
    }
}
