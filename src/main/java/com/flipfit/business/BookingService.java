package com.flipfit.business;

import com.flipfit.bean.Booking;
import java.util.List;

public interface BookingService {
    Booking createBooking(int userId, int slotId,int centerId);
    List<Booking> getBookingsByUserId(int userId);
    void cancelBooking(int bookingId);
}