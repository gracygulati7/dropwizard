package com.flipfit.api.resources;

import com.flipfit.api.dto.ApiResponse;
import com.flipfit.api.dto.CreateBookingRequest;
import com.flipfit.bean.Booking;
import com.flipfit.business.BookingService;
import com.flipfit.business.BookingServiceImpl;

import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("/bookings")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class BookingResource {

    private final BookingService bookingService = new BookingServiceImpl();

    @POST
    public Response createBooking(@Valid CreateBookingRequest request) {
        Booking booking = bookingService.createBooking(request.getUserId(), request.getSlotId(), request.getCenterId());
        if (booking == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(ApiResponse.fail("Booking failed or slot unavailable"))
                    .build();
        }
        return Response.ok(ApiResponse.ok("Booking created", booking)).build();
    }

    @GET
    @Path("/user/{userId}")
    public Response getBookingsByUser(@PathParam("userId") int userId) {
        List<Booking> bookings = bookingService.getBookingsByUserId(userId);
        return Response.ok(ApiResponse.ok("Bookings fetched", bookings)).build();
    }

    @DELETE
    @Path("/{bookingId}")
    public Response cancelBooking(@PathParam("bookingId") int bookingId) {
        bookingService.cancelBooking(bookingId);
        return Response.ok(ApiResponse.ok("Booking cancelled", null)).build();
    }
}
