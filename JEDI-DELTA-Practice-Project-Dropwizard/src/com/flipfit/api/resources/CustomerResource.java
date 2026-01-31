package com.flipfit.api.resources;

import com.flipfit.api.dto.ApiResponse;
import com.flipfit.api.dto.PaymentRequest;
import com.flipfit.api.dto.RegisterCustomerRequest;
import com.flipfit.bean.FlipFitCustomer;
import com.flipfit.dao.CustomerDAO;

import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/customers")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CustomerResource {

    private final CustomerDAO customerDAO = CustomerDAO.getInstance();

    @POST
    public Response registerCustomer(@Valid RegisterCustomerRequest request) {
        FlipFitCustomer customer = customerDAO.addCustomer(request.getFullName(), request.getEmail(), request.getPassword());
        return Response.ok(ApiResponse.ok("Customer registered", customer)).build();
    }

    @GET
    @Path("/{userId}")
    public Response getCustomer(@PathParam("userId") int userId) {
        FlipFitCustomer customer = customerDAO.getCustomerById(userId);
        if (customer == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(ApiResponse.fail("Customer not found"))
                    .build();
        }
        return Response.ok(ApiResponse.ok("Customer fetched", customer)).build();
    }

    @POST
    @Path("/{userId}/payment")
    public Response updatePayment(@PathParam("userId") int userId, @Valid PaymentRequest request) {
        customerDAO.updatePaymentDetails(userId, request.getPaymentType(), request.getPaymentInfo());
        return Response.ok(ApiResponse.ok("Payment details updated", null)).build();
    }

    @GET
    @Path("/{userId}/payment")
    public Response getPayment(@PathParam("userId") int userId) {
        FlipFitCustomer customer = customerDAO.getCustomerById(userId);
        if (customer == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(ApiResponse.fail("Customer not found"))
                    .build();
        }
        return Response.ok(ApiResponse.ok("Payment details fetched", customer)).build();
    }
}
