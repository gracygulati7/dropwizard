package com.flipfit.rest;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import com.flipfit.bean.FlipFitGymCenter;
import com.flipfit.bean.Slot;
import com.flipfit.bean.Booking;
import com.flipfit.business.GymOwnerService;
import com.flipfit.business.GymOwnerServiceImpl;
import com.flipfit.dao.GymCentreDAO;
import com.flipfit.dao.SlotDAO;
import com.flipfit.dao.BookingDAO;
import com.flipfit.rest.LoginController.ApiResponse;

/**
 * REST Controller for Gym Owner operations
 * Maps to GymOwnerMenu functionality
 */
@Path("/owners")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class GymOwnerController {
    
    private GymOwnerService gymOwnerService = new GymOwnerServiceImpl();
    private GymCentreDAO gymCentreDAO = GymCentreDAO.getInstance();
    private SlotDAO slotDAO = SlotDAO.getInstance();
    private BookingDAO bookingDAO = BookingDAO.getInstance();
    
    /**
     * Request DTOs
     */
    public static class AddGymCentreRequest {
        public String gymName;
        public String city;
        public String state;
        public Integer pincode;
        public Integer capacity;
    }
    
    public static class AddSlotRequest {
        public Integer centerId;
        public String date; // YYYY-MM-DD format
        public String startTime; // HH:MM format
        public String endTime; // HH:MM format
        public Integer seats;
    }
    
    /**
     * POST /owners/{ownerId}/centers - Add a gym centre
     */
    @POST
    @Path("/{ownerId}/centers")
    public Response addGymCentre(@PathParam("ownerId") int ownerId, AddGymCentreRequest request) {
        if (request.gymName == null || request.city == null || request.state == null || 
            request.pincode == null || request.capacity == null) {
            return Response.status(400)
                    .entity(ApiResponse.fail("All fields (gymName, city, state, pincode, capacity) are required"))
                    .build();
        }
        
        try {
            // Auto-generate centre ID
            int centerId = gymCentreDAO.getNextCentreId();
            
            gymOwnerService.addCentre(ownerId, centerId, request.gymName, request.city, 
                                     request.state, request.pincode, request.capacity);
            
            return Response.status(201)
                    .entity(ApiResponse.ok("Gym center added successfully with ID: " + centerId, null))
                    .build();
        } catch (Exception e) {
            return Response.status(500)
                    .entity(ApiResponse.fail("Error adding gym center: " + e.getMessage()))
                    .build();
        }
    }
    
    /**
     * GET /owners/{ownerId}/centers - View owner's centres
     */
    @GET
    @Path("/{ownerId}/centers")
    public Response viewMyCentres(@PathParam("ownerId") int ownerId) {
        try {
            // Get all centers (filter by ownerId if possible)
            List<FlipFitGymCenter> centers = gymCentreDAO.getAllCentres();
            List<FlipFitGymCenter> ownerCenters = new ArrayList<>();
            
            for (FlipFitGymCenter center : centers) {
                // Check if center belongs to this owner (if ownerId field exists)
                if (center.getGymId() > 0) { // Simple check - in real scenario check owner_id field
                    ownerCenters.add(center);
                }
            }
            
            if (ownerCenters.isEmpty()) {
                return Response.ok(ApiResponse.ok("No centers found for this owner", new ArrayList<>())).build();
            }
            
            gymOwnerService.viewCentres(ownerId);
            
            return Response.ok(ApiResponse.ok("Owner centers fetched", ownerCenters)).build();
        } catch (Exception e) {
            return Response.status(500)
                    .entity(ApiResponse.fail("Error fetching centers: " + e.getMessage()))
                    .build();
        }
    }
    
    /**
     * POST /owners/{ownerId}/centers/{centerId}/slots - Add slot to centre
     */
    @POST
    @Path("/{ownerId}/centers/{centerId}/slots")
    public Response addSlot(@PathParam("ownerId") int ownerId, 
                           @PathParam("centerId") int centerId,
                           AddSlotRequest request) {
        if (request.startTime == null || request.endTime == null || request.seats == null) {
            return Response.status(400)
                    .entity(ApiResponse.fail("startTime, endTime, and seats are required"))
                    .build();
        }
        
        try {
            // Verify center exists
            if (!gymCentreDAO.centreIdExists(centerId)) {
                return Response.status(404)
                        .entity(ApiResponse.fail("Gym Centre with ID " + centerId + " not found"))
                        .build();
            }
            
            // Auto-generate slot ID
            int slotId = slotDAO.getNextSlotId();
            
            // Parse date if provided
            LocalDate date = LocalDate.now();
            if (request.date != null && !request.date.isEmpty()) {
                try {
                    date = LocalDate.parse(request.date, DateTimeFormatter.ISO_LOCAL_DATE);
                } catch (Exception e) {
                    return Response.status(400)
                            .entity(ApiResponse.fail("Invalid date format. Use YYYY-MM-DD"))
                            .build();
                }
            }
            
            // Validate time format
            try {
                DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
                LocalDate.parse("2000-01-01T" + request.startTime + ":00");
                LocalDate.parse("2000-01-01T" + request.endTime + ":00");
            } catch (Exception e) {
                return Response.status(400)
                        .entity(ApiResponse.fail("Invalid time format. Use HH:MM (24-hour format)"))
                        .build();
            }
            
            // Create slot via service
            Slot newSlot = new Slot();
            newSlot.setSlotId(slotId);
            newSlot.setCenterId(centerId);
            newSlot.setStartTime(request.startTime);
            newSlot.setEndTime(request.endTime);
            newSlot.setTotalSeats(request.seats);
            newSlot.setSeatsAvailable(request.seats);
            newSlot.setDate(date);
            
            slotDAO.addSlot(slotId, centerId, request.startTime, request.endTime, request.seats, date);
            
            return Response.status(201)
                    .entity(ApiResponse.ok("Slot added successfully with ID: " + slotId, newSlot))
                    .build();
        } catch (Exception e) {
            return Response.status(500)
                    .entity(ApiResponse.fail("Error adding slot: " + e.getMessage()))
                    .build();
        }
    }
    
    /**
     * GET /owners/{ownerId}/centers/{centerId}/slots - View slots in centre
     */
    @GET
    @Path("/{ownerId}/centers/{centerId}/slots")
    public Response viewSlotsInCentre(@PathParam("ownerId") int ownerId,
                                     @PathParam("centerId") int centerId) {
        try {
            // Verify center exists
            FlipFitGymCenter center = gymCentreDAO.getGymCentreById(centerId);
            if (center == null) {
                return Response.status(404)
                        .entity(ApiResponse.fail("Gym Centre with ID " + centerId + " not found"))
                        .build();
            }
            
            List<Slot> slots = slotDAO.getSlotsByCenterId(centerId);
            
            if (slots.isEmpty()) {
                return Response.ok(ApiResponse.ok("No slots found for this center", new ArrayList<>())).build();
            }
            
            return Response.ok(ApiResponse.ok("Slots for center " + centerId + " fetched", slots)).build();
        } catch (Exception e) {
            return Response.status(500)
                    .entity(ApiResponse.fail("Error fetching slots: " + e.getMessage()))
                    .build();
        }
    }
    
    /**
     * GET /owners/{ownerId}/centers/{centerId}/customers - View customers in centre
     */
    @GET
    @Path("/{ownerId}/centers/{centerId}/customers")
    public Response viewCustomersInCentre(@PathParam("ownerId") int ownerId,
                                         @PathParam("centerId") int centerId) {
        try {
            // Verify center exists
            FlipFitGymCenter center = gymCentreDAO.getGymCentreById(centerId);
            if (center == null) {
                return Response.status(404)
                        .entity(ApiResponse.fail("Gym Centre with ID " + centerId + " not found"))
                        .build();
            }
            
            // Get all bookings for this center
            List<Booking> bookings = bookingDAO.getBookingsByCenterId(centerId);
            
            if (bookings.isEmpty()) {
                return Response.ok(ApiResponse.ok("No customers have booked slots in this center", new ArrayList<>())).build();
            }
            
            return Response.ok(ApiResponse.ok("Customers for center " + centerId + " fetched", bookings)).build();
        } catch (Exception e) {
            return Response.status(500)
                    .entity(ApiResponse.fail("Error fetching customers: " + e.getMessage()))
                    .build();
        }
    }
    
    /**
     * GET /owners/{ownerId}/profile - View owner profile
     */
    @GET
    @Path("/{ownerId}/profile")
    public Response viewProfile(@PathParam("ownerId") int ownerId) {
        try {
            gymOwnerService.viewProfile(ownerId);
            return Response.ok(ApiResponse.ok("Owner profile retrieved", null)).build();
        } catch (Exception e) {
            return Response.status(500)
                    .entity(ApiResponse.fail("Error fetching profile: " + e.getMessage()))
                    .build();
        }
    }
    
    /**
     * PUT /owners/{ownerId}/profile - Edit owner profile
     */
    @PUT
    @Path("/{ownerId}/profile")
    public Response editProfile(@PathParam("ownerId") int ownerId) {
        try {
            gymOwnerService.editDetails(ownerId);
            return Response.ok(ApiResponse.ok("Owner profile updated successfully", null)).build();
        } catch (Exception e) {
            return Response.status(500)
                    .entity(ApiResponse.fail("Error updating profile: " + e.getMessage()))
                    .build();
        }
    }
}
