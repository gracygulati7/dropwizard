package com.flipfit.api.resources;

import com.flipfit.api.dto.AddGymCenterRequest;
import com.flipfit.api.dto.AddSlotRequest;
import com.flipfit.api.dto.ApiResponse;
import com.flipfit.api.dto.RegisterOwnerRequest;
import com.flipfit.bean.FlipFitGymCenter;
import com.flipfit.bean.FlipFitGymOwner;
import com.flipfit.bean.Slot;
import com.flipfit.bean.Booking;
import com.flipfit.api.dto.AddGymCenterRequest;
import com.flipfit.api.dto.AddSlotRequest;
import com.flipfit.api.dto.ApiResponse;
import com.flipfit.api.dto.RegisterOwnerRequest;
import com.flipfit.business.GymOwnerService;
import com.flipfit.business.GymOwnerServiceImpl;
import com.flipfit.dao.BookingDAO;
import com.flipfit.dao.GymCentreDAO;
import com.flipfit.dao.OwnerDAO;
import com.flipfit.dao.SlotDAO;

import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Path("/owners")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class GymOwnerResource {

    private final GymOwnerService gymOwnerService = new GymOwnerServiceImpl();
    private final OwnerDAO ownerDAO = OwnerDAO.getInstance();
    private final GymCentreDAO gymCentreDAO = GymCentreDAO.getInstance();
    private final SlotDAO slotDAO = SlotDAO.getInstance();
    private final BookingDAO bookingDAO = BookingDAO.getInstance();

    @POST
    public Response registerOwner(@Valid RegisterOwnerRequest request) {
        gymOwnerService.registerOwner(
                request.getFullName(),
                request.getEmail(),
                request.getPassword(),
                request.getPan(),
                request.getAadhaar(),
                request.getGstin()
        );
        FlipFitGymOwner owner = ownerDAO.getOwnerByEmail(request.getEmail());
        if (owner == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(ApiResponse.fail("Owner registration failed"))
                    .build();
        }
        return Response.ok(ApiResponse.ok("Owner registered", owner)).build();
    }

    @GET
    public Response listOwners() {
        return Response.ok(ApiResponse.ok("Owners fetched", ownerDAO.getAllOwners())).build();
    }

    @GET
    @Path("/{ownerId}")
    public Response getOwner(@PathParam("ownerId") int ownerId) {
        FlipFitGymOwner owner = ownerDAO.getOwnerById(ownerId);
        if (owner == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(ApiResponse.fail("Owner not found"))
                    .build();
        }
        return Response.ok(ApiResponse.ok("Owner fetched", owner)).build();
    }

    @POST
    @Path("/{ownerId}/centers")
    public Response addCenter(@PathParam("ownerId") int ownerId, @Valid AddGymCenterRequest request) {
        FlipFitGymCenter center = new FlipFitGymCenter(
                request.getCenterId() != null ? request.getCenterId() : gymCentreDAO.getNextCentreId(),
                request.getGymName(),
                request.getCity(),
                request.getState(),
                request.getPincode(),
                request.getCapacity()
        );
        center.setOwnerId(ownerId);
        gymCentreDAO.addGymCentre(center);
        return Response.ok(ApiResponse.ok("Center added", center)).build();
    }

    @GET
    @Path("/{ownerId}/centers")
    public Response listOwnerCenters(@PathParam("ownerId") int ownerId) {
        List<FlipFitGymCenter> centers = gymCentreDAO.getAllCentres().stream()
                .filter(c -> c.getOwnerId() == ownerId)
                .collect(Collectors.toList());
        return Response.ok(ApiResponse.ok("Owner centers fetched", centers)).build();
    }

    @POST
    @Path("/{ownerId}/centers/{centerId}/slots")
    public Response addSlot(@PathParam("ownerId") int ownerId,
                            @PathParam("centerId") int centerId,
                            @Valid AddSlotRequest request) {
        FlipFitGymCenter center = gymCentreDAO.getGymCentreById(centerId);
        if (center == null || center.getOwnerId() != ownerId) {
            return Response.status(Response.Status.FORBIDDEN)
                    .entity(ApiResponse.fail("Not authorized to add slots to this center"))
                    .build();
        }

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
    @Path("/{ownerId}/centers/{centerId}/slots")
    public Response listSlots(@PathParam("ownerId") int ownerId, @PathParam("centerId") int centerId) {
        List<Slot> slots = slotDAO.getSlotsByCenterId(centerId);
        return Response.ok(ApiResponse.ok("Slots fetched", slots)).build();
    }

    @GET
    @Path("/{ownerId}/centers/{centerId}/bookings")
    public Response listBookings(@PathParam("ownerId") int ownerId, @PathParam("centerId") int centerId) {
        FlipFitGymCenter center = gymCentreDAO.getGymCentreById(centerId);
        if (center == null || center.getOwnerId() != ownerId) {
            return Response.status(Response.Status.FORBIDDEN)
                    .entity(ApiResponse.fail("You do not have access to this center"))
                    .build();
        }

        List<Slot> slots = slotDAO.getSlotsByCenterId(centerId);
        List<Booking> bookings = slots.stream()
                .flatMap(slot -> bookingDAO.getBookingsBySlotId(slot.getSlotId()).stream())
                .collect(Collectors.toList());

        return Response.ok(ApiResponse.ok("Bookings fetched", bookings)).build();
    }
}
