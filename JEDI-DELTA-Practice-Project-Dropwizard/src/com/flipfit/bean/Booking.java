package com.flipfit.bean;

import java.time.LocalDateTime;
import java.time.LocalDate;

// TODO: Auto-generated Javadoc
/**
 * The Class Booking.
 *
 * @author JEDI-DELTA
 * @ClassName  "Booking"
 */

public class Booking {

    private int bookingId;
    private int userId;
    private int slotId;
    private int centerId;
    private LocalDate slotDate;
    private String startTime;
    private String endTime;
    private boolean isDeleted;
    private LocalDateTime bookingDate;
    private BookingStatus status; // CONFIRMED, WAITLISTED, CANCELLED

    public enum BookingStatus {
        CONFIRMED, WAITLISTED, CANCELLED
    }

    /**
     * Instantiates a new booking.
     */

    public Booking() {
        this.bookingDate = LocalDateTime.now();
        this.isDeleted = false;
        this.status = BookingStatus.CONFIRMED;
    }

    /**
     * Instantiates a new booking.
     *
     * @param bookingId the booking ID
     * @param userId the user ID
     * @param slotId the slot ID
     */

    public Booking(int bookingId, int userId, int slotId) {
        this.bookingId = bookingId;
        this.userId = userId;
        this.slotId = slotId;
        this.isDeleted = false;
        this.bookingDate = LocalDateTime.now();
        this.status = BookingStatus.CONFIRMED;
    }

    /**
     * Instantiates a new booking.
     *
     * @param bookingId the booking ID
     * @param userId the user ID
     * @param slotId the slot ID
     * @param centerId the center ID
     * @param slotDate the slot date
     * @param startTime the start time
     * @param endTime the end time
     */

    public Booking(int bookingId, int userId, int slotId, int centerId, LocalDate slotDate, String startTime, String endTime) {
        this.bookingId = bookingId;
        this.userId = userId;
        this.slotId = slotId;
        this.centerId = centerId;
        this.slotDate = slotDate;
        this.startTime = startTime;
        this.endTime = endTime;
        this.isDeleted = false;
        this.bookingDate = LocalDateTime.now();
        this.status = BookingStatus.CONFIRMED;
    }

    /**
     * Gets the booking ID.
     *
     * @return the booking ID
     */

    public int getBookingId() {
        return bookingId;
    }

    /**
     * Sets the booking ID.
     *
     * @param bookingId the new booking ID
     */

    public void setBookingId(int bookingId) {
        this.bookingId = bookingId;
    }
    /**
     * Gets the user ID.
     *
     * @return the user ID
     */
    public int getUserId() {
        return userId;
    }

    /**
     * Sets the user ID.
     *
     * @param userId the new user ID
     */
    public void setUserId(int userId) {
        this.userId = userId;
    }

    /**
     * Gets the slot ID.
     *
     * @return the slot ID
     */
    public int getSlotId() {
        return slotId;
    }

    /**
     * Sets the slot ID.
     *
     * @param slotId the new slot ID
     */
    public void setSlotId(int slotId) {
        this.slotId = slotId;
    }

    /**
     * Gets the center ID.
     *
     * @return the center ID
     */
    public int getCenterId() {
        return centerId;
    }

    /**
     * Sets the center ID.
     *
     * @param centerId the new center ID
     */
    public void setCenterId(int centerId) {
        this.centerId = centerId;
    }

    /**
     * Gets the slot date.
     *
     * @return the slot date
     */
    public LocalDate getSlotDate() {
        return slotDate;
    }

    /**
     * Sets the slot date.
     *
     * @param slotDate the new slot date
     */
    public void setSlotDate(LocalDate slotDate) {
        this.slotDate = slotDate;
    }

    /**
     * Gets the start time.
     *
     * @return the start time
     */
    public String getStartTime() {
        return startTime;
    }

    /**
     * Sets the start time.
     *
     * @param startTime the new start time
     */
    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    /**
     * Gets the end time.
     *
     * @return the end time
     */
    public String getEndTime() {
        return endTime;
    }

    /**
     * Sets the end time.
     *
     * @param endTime the new end time
     */
    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    /**
     * Checks if is deleted.
     *
     * @return true, if is deleted
     */
    public boolean isDeleted() {
        return isDeleted;
    }

    /**
     * Sets the deleted.
     *
     * @param isDeleted the new deleted
     */
    public void setDeleted(boolean isDeleted) {
        this.isDeleted = isDeleted;
    }

    /**
     * Gets the booking date.
     *
     * @return the booking date
     */
    public LocalDateTime getBookingDate() {
        return bookingDate;
    }

    /**
     * Sets the booking date.
     *
     * @param bookingDate the new booking date
     */
    public void setBookingDate(LocalDateTime bookingDate) {
        this.bookingDate = bookingDate;
    }

    /**
     * Gets the status.
     *
     * @return the status
     */
    public BookingStatus getStatus() {
        return status;
    }

    /**
     * Sets the status.
     *
     * @param status the new status
     */
    public void setStatus(BookingStatus status) {
        this.status = status;
    }

    /**
     * To string.
     *
     * @return the string
     */
    @Override
    public String toString() {
        return "Booking [BookingId=" + bookingId + ", UserId=" + userId + ", SlotId=" + slotId
                + ", CenterId=" + centerId + ", SlotDate=" + slotDate + ", Time=" + startTime + "-" + endTime
                + ", Status=" + status + ", BookingDate=" + bookingDate + ", IsDeleted=" + isDeleted + "]";
    }
}