package com.flipfit.rest;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import com.flipfit.bean.Booking;
import com.flipfit.bean.FlipFitCustomer;
import com.flipfit.bean.FlipFitGymCenter;
import com.flipfit.bean.Slot;
import com.flipfit.business.BookingService;
import com.flipfit.business.BookingServiceImpl;
import com.flipfit.business.GymCentreService;
import com.flipfit.business.GymCentreServiceImpl;
import com.flipfit.business.UserService;
import com.flipfit.business.UserServiceImpl;
import com.flipfit.business.NotificationServiceImpl;
import com.flipfit.business.CustomerService;
import com.flipfit.business.CustomerServiceImpl;
import com.flipfit.dao.CustomerDAO;
import com.flipfit.dao.GymCentreDAO;
import com.flipfit.dao.SlotDAO;
import com.flipfit.rest.LoginController.ApiResponse;

/**
 * REST Controller for Customer operations
 * Maps to CustomerMenu functionality
 */
@Path("/customers")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CustomerController {
    
    private BookingService bookingService = new BookingServiceImpl();
    private GymCentreService gymCentreService = new GymCentreServiceImpl();
    private UserService userService = new UserServiceImpl();
    private GymCentreDAO gymCentreDAO = GymCentreDAO.getInstance();
    private SlotDAO slotDAO = SlotDAO.getInstance();
    private NotificationServiceImpl notificationService = NotificationServiceImpl.getInstance();
    private CustomerService customerService = new CustomerServiceImpl();
    
    /**
     * Request DTOs
     */
    public static class BookSlotRequest {
        public int userId;
        public int centerId;
        public int slotId;
        public String bookingDate; // YYYY-MM-DD format
        public double paymentAmount;
    }
    
    public static class CancelBookingRequest {
        public int userId;
        public int bookingId;
    }
    
    /**
     * GET /customers/{userId}/gyms - View available gyms
     */
    @GET
    @Path("/{userId}/gyms")
    public Response viewGyms(@PathParam("userId") int userId) {
        try {
            List<FlipFitGymCenter> gyms = gymCentreDAO.getAllCentres();
            
            if (gyms.isEmpty()) {
                return Response.ok(ApiResponse.ok("No gyms available", new ArrayList<>())).build();
            }
            
            List<String> gymList = new ArrayList<>();
            for (FlipFitGymCenter gym : gyms) {
                gymList.add("ID: " + gym.getGymId() + ", Name: " + gym.getGymName() + ", Location: " + gym.getLocation());
                List<Slot> slots = gymCentreService.getSlotsByCentreId(gym.getGymId());
                for (Slot slot : slots) {
                    String dateStr = (slot.getDate() != null) ? slot.getDate().toString() : "N/A";
                    gymList.add("  Slot: " + slot + " | Date: " + dateStr);
                }
            }
            
            return Response.ok(ApiResponse.ok("Available gyms fetched", gyms)).build();
        } catch (Exception e) {
            return Response.status(500)
                    .entity(ApiResponse.fail("Error fetching gyms: " + e.getMessage()))
                    .build();
        }
    }
    
    /**
     * GET /customers/{userId}/bookings - View customer's bookings
     */
    @GET
    @Path("/{userId}/bookings")
    public Response viewMyBookings(@PathParam("userId") int userId) {
        try {
            List<Booking> bookings = bookingService.getBookingsByUserId(userId);
            
            if (bookings.isEmpty()) {
                return Response.ok(ApiResponse.ok("No active bookings found", new ArrayList<>())).build();
            }
            
            List<String> bookingDetails = new ArrayList<>();
            for (Booking booking : bookings) {
                Slot slot = slotDAO.getSlotById(booking.getUserId(), booking.getSlotId(), booking.getCenterId());
                if (slot != null) {
                    FlipFitGymCenter gym = gymCentreDAO.getGymCentreById(booking.getCenterId());
                    String gymName = (gym != null) ? gym.getGymName() : "Unknown Gym";
                    bookingDetails.add("Booking #" + booking.getBookingId() + " | Gym: " + gymName + 
                                     " | Date: " + booking.getSlotDate() + " | Time: " + booking.getStartTime());
                }
            }
            
            return Response.ok(ApiResponse.ok("Customer bookings fetched", bookings)).build();
        } catch (Exception e) {
            return Response.status(500)
                    .entity(ApiResponse.fail("Error fetching bookings: " + e.getMessage()))
                    .build();
        }
    }
    
    /**
     * POST /customers/{userId}/book-slot - Book a slot
     */
    @POST
    @Path("/{userId}/book-slot")
    public Response bookSlot(@PathParam("userId") int userId, BookSlotRequest request) {
        try {
            if (request.centerId <= 0 || request.slotId <= 0) {
                return Response.status(400)
                        .entity(ApiResponse.fail("Valid centerId and slotId are required"))
                        .build();
            }
            
            // Validate center exists
            FlipFitGymCenter selectedCenter = gymCentreDAO.getGymCentreById(request.centerId);
            if (selectedCenter == null) {
                return Response.status(404)
                        .entity(ApiResponse.fail("Invalid Center ID"))
                        .build();
            }
            
            // Parse booking date if provided
            LocalDate bookingDate = LocalDate.now();
            if (request.bookingDate != null && !request.bookingDate.isEmpty()) {
                try {
                    bookingDate = LocalDate.parse(request.bookingDate, DateTimeFormatter.ISO_LOCAL_DATE);
                } catch (Exception e) {
                    return Response.status(400)
                            .entity(ApiResponse.fail("Invalid date format. Use YYYY-MM-DD"))
                            .build();
                }
            }
            
            // Get all slots for this center and date
            List<Slot> slotsForCenterAndDate = new ArrayList<>();
            List<Slot> fullSlotsForCenterAndDate = new ArrayList<>();
            
            List<Slot> allSlots = slotDAO.getAllSlots();
            for (Slot slot : allSlots) {
                if (slot.getDate() != null && slot.getDate().equals(bookingDate) && 
                    slot.getCenterId() == request.centerId && !slot.isExpired()) {
                    
                    if (slot.getSeatsAvailable() > 0) {
                        slotsForCenterAndDate.add(slot);
                    } else {
                        fullSlotsForCenterAndDate.add(slot);
                    }
                }
            }
            
            // Validate slot exists
            Slot selectedSlot = null;
            boolean isFullSlot = false;
            
            for (Slot slot : slotsForCenterAndDate) {
                if (slot.getSlotId() == request.slotId) {
                    selectedSlot = slot;
                    break;
                }
            }
            
            if (selectedSlot == null) {
                for (Slot slot : fullSlotsForCenterAndDate) {
                    if (slot.getSlotId() == request.slotId) {
                        selectedSlot = slot;
                        isFullSlot = true;
                        break;
                    }
                }
            }
            
            if (selectedSlot == null) {
                return Response.status(404)
                        .entity(ApiResponse.fail("Invalid Slot ID or slot not available for selected date"))
                        .build();
            }
            
            // Process payment for non-full slots
            if (!isFullSlot && request.paymentAmount > 0) {
                boolean paymentSuccess = customerService.makePayment(userId, (int) request.paymentAmount);
                if (!paymentSuccess) {
                    return Response.status(400)
                            .entity(ApiResponse.fail("Payment failed. Booking cancelled"))
                            .build();
                }
            }
            
            // Create booking
            Booking booking = bookingService.createBooking(userId, request.slotId, request.centerId);
            
            if (booking != null) {
                if (isFullSlot) {
                    return Response.status(201)
                            .entity(ApiResponse.ok("Added to waitlist", booking))
                            .build();
                } else {
                    // Update seat count in database
                    int newSeatCount = selectedSlot.getSeatsAvailable() - 1;
                    boolean isDbUpdated = slotDAO.updateSlotSeats(request.slotId, newSeatCount);
                    if (isDbUpdated) {
                        selectedSlot.setSeatsAvailable(newSeatCount);
                    }
                    
                    return Response.status(201)
                            .entity(ApiResponse.ok("Booking confirmed", booking))
                            .build();
                }
            } else {
                return Response.status(400)
                        .entity(ApiResponse.fail("Booking failed. Time conflict or invalid slot"))
                        .build();
            }
            
        } catch (Exception e) {
            return Response.status(500)
                    .entity(ApiResponse.fail("Error booking slot: " + e.getMessage()))
                    .build();
        }
    }
    
    /**
     * DELETE /customers/{userId}/bookings/{bookingId} - Cancel booking
     */
    @DELETE
    @Path("/{userId}/bookings/{bookingId}")
    public Response cancelBooking(@PathParam("userId") int userId, @PathParam("bookingId") int bookingId) {
        try {
            // Verify booking belongs to user
            List<Booking> myBookings = bookingService.getBookingsByUserId(userId);
            boolean exists = myBookings.stream().anyMatch(b -> b.getBookingId() == bookingId);
            
            if (!exists) {
                return Response.status(404)
                        .entity(ApiResponse.fail("Booking ID not found in your active bookings"))
                        .build();
            }
            
            bookingService.cancelBooking(bookingId);
            
            return Response.ok(ApiResponse.ok("Booking cancelled successfully", null)).build();
        } catch (Exception e) {
            return Response.status(500)
                    .entity(ApiResponse.fail("Error cancelling booking: " + e.getMessage()))
                    .build();
        }
    }
    
    /**
     * GET /customers/{userId}/notifications - View notifications
     */
    @GET
    @Path("/{userId}/notifications")
    public Response viewNotifications(@PathParam("userId") int userId) {
        try {
            notificationService.printUserNotifications(userId);
            return Response.ok(ApiResponse.ok("Notifications retrieved", null)).build();
        } catch (Exception e) {
            return Response.status(500)
                    .entity(ApiResponse.fail("Error fetching notifications: " + e.getMessage()))
                    .build();
        }
    }
    
    /**
     * GET /customers/{userId}/available-slots - View available slots
     */
    @GET
    @Path("/{userId}/available-slots")
    public Response viewAvailableSlots(@PathParam("userId") int userId) {
        try {
            List<FlipFitGymCenter> gyms = gymCentreDAO.getAllCentres();
            
            if (gyms.isEmpty()) {
                return Response.ok(ApiResponse.ok("No gyms available", new ArrayList<>())).build();
            }
            
            List<String> slotsList = new ArrayList<>();
            for (FlipFitGymCenter gym : gyms) {
                List<Slot> slots = userService.findAvailableSlots(gym.getGymId());
                slotsList.add("Gym: " + gym.getGymName() + " (ID: " + gym.getGymId() + ")");
                if (slots.isEmpty()) {
                    slotsList.add("  └─ No available slots");
                } else {
                    for (Slot slot : slots) {
                        slotsList.add("  Slot ID: " + slot.getSlotId() + " | Date: " + slot.getDate() + 
                                    " | Time: " + slot.getStartTime() + "-" + slot.getEndTime() + 
                                    " | Seats: " + slot.getSeatsAvailable() + "/" + slot.getTotalSeats());
                    }
                }
            }
            
            return Response.ok(ApiResponse.ok("Available slots fetched", gyms)).build();
        } catch (Exception e) {
            return Response.status(500)
                    .entity(ApiResponse.fail("Error fetching available slots: " + e.getMessage()))
                    .build();
        }
    }
    
    /**
     * GET /customers/{userId}/profile - View customer profile
     */
    @GET
    @Path("/{userId}/profile")
    public Response viewProfile(@PathParam("userId") int userId) {
        try {
            userService.viewProfile(userId);
            return Response.ok(ApiResponse.ok("Profile retrieved", null)).build();
        } catch (Exception e) {
            return Response.status(500)
                    .entity(ApiResponse.fail("Error fetching profile: " + e.getMessage()))
                    .build();
        }
    }
    
    /**
     * PUT /customers/{userId}/profile - Edit customer profile
     */
    @PUT
    @Path("/{userId}/profile")
    public Response editProfile(@PathParam("userId") int userId) {
        try {
            userService.editProfile(userId);
            return Response.ok(ApiResponse.ok("Profile updated successfully", null)).build();
        } catch (Exception e) {
            return Response.status(500)
                    .entity(ApiResponse.fail("Error updating profile: " + e.getMessage()))
                    .build();
        }
    }
    
    /**
     * GET /customers/{userId}/payment-info - View payment information
     */
    @GET
    @Path("/{userId}/payment-info")
    public Response viewPaymentInfo(@PathParam("userId") int userId) {
        try {
            customerService.viewPaymentInfo(userId);
            return Response.ok(ApiResponse.ok("Payment info retrieved", null)).build();
        } catch (Exception e) {
            return Response.status(500)
                    .entity(ApiResponse.fail("Error fetching payment info: " + e.getMessage()))
                    .build();
        }
    }
}
