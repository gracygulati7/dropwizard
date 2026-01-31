package com.flipfit.api.resources;

import com.flipfit.api.dto.AddGymCenterRequest;
import com.flipfit.api.dto.AddSlotRequest;
import com.flipfit.api.dto.AdminLoginRequest;
import com.flipfit.api.dto.ApiResponse;
import com.flipfit.bean.FlipFitGymCenter;
import com.flipfit.bean.Slot;
import com.flipfit.business.AdminService;
import com.flipfit.business.AdminServiceImpl;
import com.flipfit.dao.AdminDAO;
import com.flipfit.dao.CustomerDAO;
import com.flipfit.dao.GymCentreDAO;
import com.flipfit.dao.OwnerDAO;
import com.flipfit.dao.SlotDAO;

import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.time.LocalDate;
import java.util.List;

@Path("/admin")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AdminResource {

    private final AdminDAO adminDAO = AdminDAO.getInstance();
    private final AdminService adminService = new AdminServiceImpl();
    private final GymCentreDAO gymCentreDAO = GymCentreDAO.getInstance();
    private final SlotDAO slotDAO = SlotDAO.getInstance();
    private final OwnerDAO ownerDAO = OwnerDAO.getInstance();
    private final CustomerDAO customerDAO = CustomerDAO.getInstance();

    @POST
    @Path("/login")
    public Response login(@Valid AdminLoginRequest request) {
        boolean ok = adminDAO.login(request.getEmail(), request.getPassword());
        if (!ok) {
            return Response.status(Response.Status.UNAUTHORIZED)
                    .entity(ApiResponse.fail("Invalid admin credentials"))
                    .build();
        }
        return Response.ok(ApiResponse.ok("Admin login successful", null)).build();
    }

    @POST
    @Path("/owners/{ownerId}/validate")
    public Response validateOwner(@PathParam("ownerId") int ownerId) {
        adminService.validateOwner(ownerId);
        return Response.ok(ApiResponse.ok("Owner validated", null)).build();
    }

    @POST
    @Path("/owners/{ownerId}/approve")
    public Response approveOwner(@PathParam("ownerId") int ownerId) {
        adminService.approveOwner(ownerId);
        return Response.ok(ApiResponse.ok("Owner approved", null)).build();
    }

    @POST
    @Path("/centers")
    public Response addGymCenter(@Valid AddGymCenterRequest request) {
        FlipFitGymCenter center = new FlipFitGymCenter(
                request.getCenterId() != null ? request.getCenterId() : gymCentreDAO.getNextCentreId(),
                request.getGymName(),
                request.getCity(),
                request.getState(),
                request.getPincode(),
                request.getCapacity()
        );
        if (request.getOwnerId() != null) {
            center.setOwnerId(request.getOwnerId());
        }
        gymCentreDAO.addGymCentre(center);
        return Response.ok(ApiResponse.ok("Gym center added", center)).build();
    }

    @GET
    @Path("/centers")
    public Response listCenters() {
        List<FlipFitGymCenter> centers = gymCentreDAO.getAllCentres();
        return Response.ok(ApiResponse.ok("Centers fetched", centers)).build();
    }

    @POST
    @Path("/centers/{centerId}/slots")
    public Response addSlot(@PathParam("centerId") int centerId, @Valid AddSlotRequest request) {
        LocalDate date = request.getDate() == null ? LocalDate.now() : LocalDate.parse(request.getDate());
        Slot slot = new Slot(
                request.getSlotId() != null ? request.getSlotId() : slotDAO.getNextSlotId(),
                centerId,
                date,
                request.getStartTime(),
                request.getEndTime(),
                request.getSeats()
        );
        slotDAO.addSlot(slot);
        return Response.ok(ApiResponse.ok("Slot added", slot)).build();
    }

    @GET
    @Path("/centers/{centerId}/slots")
    public Response listSlots(@PathParam("centerId") int centerId) {
        return Response.ok(ApiResponse.ok("Slots fetched", slotDAO.getSlotsByCenterId(centerId))).build();
    }

    @GET
    @Path("/owners")
    public Response listAllOwners() {
        return Response.ok(ApiResponse.ok("Owners fetched", ownerDAO.getAllOwners())).build();
    }

    @GET
    @Path("/owners/{ownerId}")
    public Response getOwner(@PathParam("ownerId") int ownerId) {
        var owner = ownerDAO.getOwnerById(ownerId);
        if (owner == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(ApiResponse.fail("Owner not found"))
                    .build();
        }
        return Response.ok(ApiResponse.ok("Owner fetched", owner)).build();
    }

    @DELETE
    @Path("/owners/{ownerId}")
    public Response deleteOwner(@PathParam("ownerId") int ownerId) {
        adminService.deleteOwner(ownerId);
        return Response.ok(ApiResponse.ok("Owner deleted", null)).build();
    }

    @GET
    @Path("/customers")
    public Response listAllCustomers() {
        return Response.ok(ApiResponse.ok("Customers fetched", customerDAO.getAllCustomers())).build();
    }

    @GET
    @Path("/customers/{customerId}")
    public Response getCustomer(@PathParam("customerId") int customerId) {
        var customer = customerDAO.getCustomerById(customerId);
        if (customer == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(ApiResponse.fail("Customer not found"))
                    .build();
        }
        return Response.ok(ApiResponse.ok("Customer fetched", customer)).build();
    }
}
