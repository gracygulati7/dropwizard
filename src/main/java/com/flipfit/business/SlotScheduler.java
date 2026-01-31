package com.flipfit.business;

import com.flipfit.bean.Booking;
import com.flipfit.bean.Slot;
import com.flipfit.dao.BookingDAO;
import com.flipfit.dao.SlotDAO;
import com.flipfit.dao.WaitlistDAO;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class SlotScheduler {
	private final BookingDAO bookingDAO = BookingDAO.getInstance();
	private final SlotDAO slotDAO = SlotDAO.getInstance();
	private final WaitlistDAO waitlistDAO = WaitlistDAO.getInstance();
	private final NotificationService notificationService = NotificationServiceImpl.getInstance();
	private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");

	public void monitorCancellations(int bookingId) {
	    Booking booking = bookingDAO.getBookingById(bookingId);
	    if (booking == null || booking.isDeleted() || booking.getStatus() == Booking.BookingStatus.CANCELLED) {
	        return;
	    }

	    int slotId = booking.getSlotId();
	    int centerId = booking.getCenterId();
	    int userId = booking.getUserId(); // Get the user ID to notify them

	    bookingDAO.cancelBooking(bookingId); 
	    System.out.println("[SCHEDULER] Booking " + bookingId + " marked as cancelled");

	    // TRIGGER THE NOTIFICATION HERE
	    notificationService.sendCancellationNotification(userId, slotId, centerId);

	    Slot slot = slotDAO.getSlotById(slotId); 
	    if (slot != null) {
	        slot.setSeatsAvailable(slot.getSeatsAvailable() + 1);
	        if (waitlistDAO.hasWaitlistedCustomers(slotId)) {
	            triggerWaitlistPromotion(slotId, centerId);
	        }
	    }
	}

	public void triggerWaitlistPromotion(int slotId, int centerId) {
		Integer nextUserId = waitlistDAO.removeFromWaitlist(slotId);
		if (nextUserId == null)
			return;

		Slot slot = slotDAO.getSlotById(slotId);
		if (slot == null || slot.getSeatsAvailable() <= 0) {
			waitlistDAO.addToWaitlist(slotId, nextUserId);
			return;
		}

		if (hasTimeConflict(nextUserId, slot.getDate(), slot.getStartTime(), slot.getEndTime(), centerId)) {
			waitlistDAO.addToWaitlist(slotId, nextUserId);
			return;
		}

		CustomerService customerService = new CustomerServiceImpl();
		if (customerService.makePayment(nextUserId, slot.getFee())) {
			slot.setSeatsAvailable(slot.getSeatsAvailable() - 1);
			Booking promotion = bookingDAO.createBooking(nextUserId, slotId);
			promotion.setStatus(Booking.BookingStatus.CONFIRMED);
			promotion.setCenterId(centerId);
			notificationService.sendWaitlistPromotion(nextUserId, slotId, centerId);
		} else {
			waitlistDAO.addToWaitlist(slotId, nextUserId);
		}
	}

	private boolean hasTimeConflict(int userId, LocalDate date, String startTime, String endTime, int newCenterId) {
		List<Booking> userBookings = bookingDAO.getBookingsByUserId(userId);
		LocalTime newStart = parseTime(startTime);
		LocalTime newEnd = parseTime(endTime);

		for (Booking booking : userBookings) {
			if (booking.isDeleted() || booking.getStatus() == Booking.BookingStatus.CANCELLED)
				continue;
			if (booking.getSlotDate() != null && booking.getSlotDate().equals(date)) {
				if (timesOverlap(newStart, newEnd, parseTime(booking.getStartTime()),
						parseTime(booking.getEndTime()))) {
					return true;
				}
			}
		}
		return false;
	}

	private boolean timesOverlap(LocalTime start1, LocalTime end1, LocalTime start2, LocalTime end2) {
		return start1.isBefore(end2) && start2.isBefore(end1);
	}

	private LocalTime parseTime(String timeStr) {
		try {
			return LocalTime.parse(timeStr, TIME_FORMATTER);
		} catch (Exception e) {
			return LocalTime.MIDNIGHT;
		}
	}

	public boolean validateBooking(int userId, int slotId, int centerId) {
		Slot slot = slotDAO.getSlotById(slotId);
		if (slot == null || slot.isExpired())
			return false;
		if (slot.getSeatsAvailable() <= 0) {
			waitlistDAO.addToWaitlist(slotId, userId);
			return false;
		}
		return !hasTimeConflict(userId, slot.getDate(), slot.getStartTime(), slot.getEndTime(), centerId);
	}

	public List<Booking> getUserBookingsForDate(int userId, LocalDate date) {
		List<Booking> activeBookings = new ArrayList<>();
		for (Booking b : bookingDAO.getBookingsByUserId(userId)) {
			if (!b.isDeleted() && b.getStatus() == Booking.BookingStatus.CONFIRMED && b.getSlotDate().equals(date)) {
				activeBookings.add(b);
			}
		}
		return activeBookings;
	}
}