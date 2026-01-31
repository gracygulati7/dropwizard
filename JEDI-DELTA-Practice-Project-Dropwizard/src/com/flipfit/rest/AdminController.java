package com.flipfit.rest;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import com.flipfit.business.AdminService;
import com.flipfit.business.AdminServiceImpl;
import com.flipfit.business.UserService;
import com.flipfit.business.UserServiceImpl;
import com.flipfit.dao.GymCentreDAO;
import com.flipfit.dao.SlotDAO;
import com.flipfit.bean.FlipFitGymCenter;
import com.flipfit.bean.Slot;
import com.flipfit.rest.LoginController.ApiResponse;

/**
 * REST Controller for Admin operations
 * Maps to AdminMenu functionality
 */
@Path("/admin")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AdminController {
    
    private AdminService adminService = new AdminServiceImpl();
    private UserService userService = new UserServiceImpl();
    private GymCentreDAO gymCentreDAO = GymCentreDAO.getInstance();
    private SlotDAO slotDAO = SlotDAO.getInstance();
    
    /**
     * Request DTOs
     */
    public static class AddGymCenterRequest {
        public Integer centerId;
        public String gymName;
        public String city;
        public String state;
        public Integer pincode;
        public Integer capacity;
    }
    
    public static class AddSlotRequest {
        public Integer centerId;
        public Integer slotId;
        public String startTime;
        public String endTime;
        public Integer seats;
    }
    
    /**
     * GET /admin/owners - View all gym owners
     */
    @GET
    @Path("/owners")
    public Response viewAllGymOwners() {
        try {
            adminService.viewAllGymOwners();
            return Response.ok(ApiResponse.ok("All gym owners fetched", null)).build();
        } catch (Exception e) {
            return Response.status(500)
                    .entity(ApiResponse.fail("Error fetching gym owners: " + e.getMessage()))
                    .build();
        }
    }
    
    /**
     * POST /admin/owners/{ownerId}/approve - Approve gym owner
     */
    @POST
    @Path("/owners/{ownerId}/approve")
    public Response approveOwner(@PathParam("ownerId") int ownerId) {
        try {
            adminService.approveOwner(ownerId);
            return Response.ok(ApiResponse.ok("Gym owner " + ownerId + " approved successfully", null)).build();
        } catch (Exception e) {
            return Response.status(500)
                    .entity(ApiResponse.fail("Error approving owner: " + e.getMessage()))
                    .build();
        }
    }
    
    /**
     * POST /admin/owners/{ownerId}/validate - Validate gym owner
     */
    @POST
    @Path("/owners/{ownerId}/validate")
    public Response validateOwner(@PathParam("ownerId") int ownerId) {
        try {
            adminService.validateOwner(ownerId);
            return Response.ok(ApiResponse.ok("Gym owner " + ownerId + " validated successfully", null)).build();
        } catch (Exception e) {
            return Response.status(500)
                    .entity(ApiResponse.fail("Error validating owner: " + e.getMessage()))
                    .build();
        }
    }
    
    /**
     * DELETE /admin/owners/{ownerId} - Delete gym owner
     */
    @DELETE
    @Path("/owners/{ownerId}")
    public Response deleteOwner(@PathParam("ownerId") int ownerId) {
        try {
            adminService.deleteOwner(ownerId);
            return Response.ok(ApiResponse.ok("Gym owner " + ownerId + " deleted successfully", null)).build();
        } catch (Exception e) {
            return Response.status(500)
                    .entity(ApiResponse.fail("Error deleting owner: " + e.getMessage()))
                    .build();
        }
    }
    
    /**
     * GET /admin/customers - View all customers
     */
    @GET
    @Path("/customers")
    public Response viewAllCustomers() {
        try {
            adminService.viewFFCustomers();
            return Response.ok(ApiResponse.ok("All customers fetched", null)).build();
        } catch (Exception e) {
            return Response.status(500)
                    .entity(ApiResponse.fail("Error fetching customers: " + e.getMessage()))
                    .build();
        }
    }
    
    /**
     * POST /admin/centers - Add a gym center
     */
    @POST
    @Path("/centers")
    public Response addGymCenter(AddGymCenterRequest request) {
        if (request.gymName == null || request.city == null || request.state == null || 
            request.pincode == null || request.capacity == null) {
            return Response.status(400)
                    .entity(ApiResponse.fail("gymName, city, state, pincode, and capacity are required"))
                    .build();
        }
        
        try {
            // Use provided centerId or generate next available ID
            int centerId = (request.centerId != null) ? request.centerId : gymCentreDAO.getNextCentreId();
            
            adminService.addGymCenter(centerId, request.gymName, request.city, request.state, 
                                     request.pincode, request.capacity);
            
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
     * GET /admin/centers - View all gym centers with slots
     */
    @GET
    @Path("/centers")
    public Response viewGymCentersWithSlots() {
        try {
            List<FlipFitGymCenter> centers = gymCentreDAO.getAllCentres();
            
            if (centers.isEmpty()) {
                return Response.ok(ApiResponse.ok("No gym centers found", centers)).build();
            }
            
            // Build response with centers and their slots
            StringBuilder response = new StringBuilder();
            response.append("Available gym centers:\n");
            
            for (FlipFitGymCenter center : centers) {
                response.append("Center ID: ").append(center.getGymId())
                        .append(" | Name: ").append(center.getGymName())
                        .append(" | Location: ").append(center.getLocation()).append("\n");
                
                List<Slot> slots = slotDAO.getSlotsByCenterId(center.getGymId());
                if (slots.isEmpty()) {
                    response.append("  └─ No slots available\n");
                } else {
                    response.append("  └─ Slots:\n");
                    for (Slot slot : slots) {
                        String dateStr = (slot.getDate() != null) ? slot.getDate().toString() : "N/A";
                        response.append("     • Slot ID: ").append(slot.getSlotId())
                                .append(" | Date: ").append(dateStr)
                                .append(" | Time: ").append(slot.getStartTime())
                                .append(" - ").append(slot.getEndTime())
                                .append(" | Available Seats: ").append(slot.getSeatsAvailable()).append("\n");
                    }
                }
            }
            
            return Response.ok(ApiResponse.ok("Gym centers fetched successfully", centers)).build();
        } catch (Exception e) {
            return Response.status(500)
                    .entity(ApiResponse.fail("Error fetching gym centers: " + e.getMessage()))
                    .build();
        }
    }
    
    /**
     * POST /admin/centers/{centerId}/slots - Add slot to a center
     */
    @POST
    @Path("/centers/{centerId}/slots")
    public Response addSlot(@PathParam("centerId") int centerId, AddSlotRequest request) {
        if (request.startTime == null || request.endTime == null || request.seats == null) {
            return Response.status(400)
                    .entity(ApiResponse.fail("startTime, endTime, and seats are required"))
                    .build();
        }
        
        try {
            // Use provided slotId or generate next available ID
            int slotId = (request.slotId != null) ? request.slotId : slotDAO.getNextSlotId();
            
            adminService.addSlotInfo(centerId, slotId, request.startTime, request.endTime, request.seats);
            
            return Response.status(201)
                    .entity(ApiResponse.ok("Slot added successfully with ID: " + slotId, null))
                    .build();
        } catch (Exception e) {
            return Response.status(500)
                    .entity(ApiResponse.fail("Error adding slot: " + e.getMessage()))
                    .build();
        }
    }
    
    /**
     * GET /admin/centers/{centerId}/slots - View slots for a center
     */
    @GET
    @Path("/centers/{centerId}/slots")
    public Response viewSlots(@PathParam("centerId") int centerId) {
        try {
            adminService.viewSlots(centerId);
            return Response.ok(ApiResponse.ok("Slots for center " + centerId + " fetched", null)).build();
        } catch (Exception e) {
            return Response.status(500)
                    .entity(ApiResponse.fail("Error fetching slots: " + e.getMessage()))
                    .build();
        }
    }
    
    /**
     * GET /admin/available-slots - View available slots across all centers
     */
    @GET
    @Path("/available-slots")
    public Response viewAvailableSlots() {
        try {
            List<FlipFitGymCenter> centers = gymCentreDAO.getAllCentres();
            
            if (centers.isEmpty()) {
                return Response.ok(ApiResponse.ok("No gym centers available", centers)).build();
            }
            
            StringBuilder response = new StringBuilder();
            response.append("Available slots across all centers:\n");
            
            for (FlipFitGymCenter center : centers) {
                response.append("Gym: ").append(center.getGymName())
                        .append(" (ID: ").append(center.getGymId()).append(")\n");
                
                List<Slot> slots = userService.findAvailableSlots(center.getGymId());
                if (slots.isEmpty()) {
                    response.append("  └─ No available slots\n");
                } else {
                    for (Slot slot : slots) {
                        response.append("     • Slot ID: ").append(slot.getSlotId())
                                .append(" | Date: ").append(slot.getDate())
                                .append(" | Time: ").append(slot.getStartTime())
                                .append(" - ").append(slot.getEndTime())
                                .append(" | Seats: ").append(slot.getSeatsAvailable())
                                .append("/").append(slot.getTotalSeats()).append("\n");
                    }
                }
            }
            
            return Response.ok(ApiResponse.ok("Available slots fetched", centers)).build();
        } catch (Exception e) {
            return Response.status(500)
                    .entity(ApiResponse.fail("Error fetching available slots: " + e.getMessage()))
                    .build();
        }
    }
    
    /**
     * GET /admin/profile/{adminId} - View admin profile
     */
    @GET
    @Path("/profile/{adminId}")
    public Response viewProfile(@PathParam("adminId") int adminId) {
        try {
            userService.viewProfile(adminId);
            return Response.ok(ApiResponse.ok("Admin profile retrieved", null)).build();
        } catch (Exception e) {
            return Response.status(500)
                    .entity(ApiResponse.fail("Error fetching profile: " + e.getMessage()))
                    .build();
        }
    }
    
    /**
     * PUT /admin/profile/{adminId} - Edit admin profile
     */
    @PUT
    @Path("/profile/{adminId}")
    public Response editProfile(@PathParam("adminId") int adminId) {
        try {
            userService.editProfile(adminId);
            return Response.ok(ApiResponse.ok("Admin profile updated", null)).build();
        } catch (Exception e) {
            return Response.status(500)
                    .entity(ApiResponse.fail("Error updating profile: " + e.getMessage()))
                    .build();
        }
    }
}
