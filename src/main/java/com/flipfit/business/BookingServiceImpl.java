package com.flipfit.business;

import com.flipfit.bean.Booking;
import com.flipfit.bean.Slot;
import com.flipfit.dao.BookingDAO;
import com.flipfit.dao.SlotDAO;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public class BookingServiceImpl implements BookingService {
	private final BookingDAO bookingDAO = BookingDAO.getInstance();
	private final SlotDAO slotDAO = SlotDAO.getInstance();
	private final SlotScheduler slotScheduler = new SlotScheduler();
	private final NotificationService notificationService = NotificationServiceImpl.getInstance();

	@Override
	public Booking createBooking(int userId, int slotId, int centerId) {
		Slot slot = slotDAO.getSlotById(slotId);
		if (slot == null || !slotScheduler.validateBooking(userId, slotId, centerId))
			return null;

		slot.setSeatsAvailable(slot.getSeatsAvailable() - 1);
		Booking booking = bookingDAO.createBooking(userId, slotId);
		booking.setCenterId(centerId);
		booking.setSlotDate(slot.getDate());
		booking.setStartTime(slot.getStartTime());
		booking.setEndTime(slot.getEndTime());
		booking.setStatus(Booking.BookingStatus.CONFIRMED);

		notificationService.sendBookingConfirmation(userId, slotId, centerId);
		return booking;
	}

	@Override
	public List<Booking> getBookingsByUserId(int userId) {
		// Only return bookings that aren't cancelled or deleted
		return bookingDAO.getBookingsByUserId(userId).stream()
				.filter(b -> !b.isDeleted() && b.getStatus() == Booking.BookingStatus.CONFIRMED)
				.collect(Collectors.toList());
	}

	@Override
	public void cancelBooking(int bookingId) {
		slotScheduler.monitorCancellations(bookingId);
	}
}