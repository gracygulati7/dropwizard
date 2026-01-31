package com.flipfit.bean;

import java.time.LocalDate;

// TODO: Auto-generated Javadoc
/**
 * The Class Slot.
 *
 * Represents a time slot at a gym centre including date, time and seat availability.
 *
 * @author JEDI-DELTA
 * @ClassName  "Slot"
 */
public class Slot {

    private int slotId;
    private int centerId;
    private LocalDate date;
    private String startTime;
    private String endTime;
    private int seatsAvailable;
    private int totalSeats;
    private int fee;

    /**
     * Instantiates a new slot.
     */
    public Slot() {

    }

    /**
     * Instantiates a new slot with given values.
     *
     * @param slotId the slot id
     * @param centerId the center id
     * @param date the date of the slot
     * @param startTime the start time
     * @param endTime the end time
     * @param seatsAvailable number of available seats (initially total seats)
     */
    public Slot(int slotId, int centerId, LocalDate date, String startTime, String endTime, int seatsAvailable) {
        this.slotId = slotId;
        this.centerId = centerId;
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
        this.seatsAvailable = seatsAvailable;
        this.totalSeats = seatsAvailable;
    }

    /**
     * Gets the slot id.
     *
     * @return the slot id
     */
    public int getSlotId() {
        return slotId;
    }

    /**
     * Sets the slot id.
     *
     * @param slotId the new slot id
     */
    public void setSlotId(int slotId) {
        this.slotId = slotId;
    }

    /**
     * Gets the centre id.
     *
     * @return the centre id
     */
    public int getCenterId() {
        return centerId;
    }

    /**
     * Sets the centre id.
     *
     * @param centerId the new centre id
     */
    public void setCenterId(int centerId) {
        this.centerId = centerId;
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
     * Gets the seats available.
     *
     * @return the seats available
     */
    public int getSeatsAvailable() {
        return seatsAvailable;
    }

    /**
     * Sets the seats available.
     *
     * @param seatsAvailable the new seats available
     */
    public void setSeatsAvailable(int seatsAvailable) {
        this.seatsAvailable = seatsAvailable;
    }

    /**
     * Gets the total seats.
     *
     * @return the total seats
     */
    public int getTotalSeats() {
        return totalSeats;
    }

    /**
     * Sets the total seats.
     *
     * @param totalSeats the new total seats
     */
    public void setTotalSeats(int totalSeats) {
        this.totalSeats = totalSeats;
    }

    /**
     * Checks whether this slot has expired (date is before today).
     *
     * @return true if expired
     */
    public boolean isExpired() {
        return LocalDate.now().isAfter(this.date);
    }

    /**
     * Gets the date.
     *
     * @return the date
     */
    public LocalDate getDate() {
        return date;
    }

    /**
     * Sets the date.
     *
     * @param date the new date
     */
    public void setDate(LocalDate date) {
        this.date = date;
    }

    /**
     * Gets the number of booked seats.
     *
     * @return the booked seats
     */
    public int getBookedSeats() {
        return totalSeats - seatsAvailable;
    }

    /**
     * Checks if slot is full.
     *
     * @return true if full
     */
    public boolean isFull() {
        return seatsAvailable <= 0;
    }
    
    /**
     * Gets the fee for the slot.
     *
     * @return the fee
     */
    public int getFee() {
        return fee;
    }
    
    /**
     * Sets the fee for the slot.
     *
     * @param fee the new fee
     */
    public void setFee(int fee) {
        this.fee = fee;
    }

    /**
     * Returns a string representation of the slot.
     *
     * @return the string
     */
    @Override
    public String toString() {
        return "SlotId=" + slotId +
               ", Date=" + (date != null ? date.toString() : "N/A") +
               ", Time: " + startTime + " - " + endTime +
               ", SeatsAvailable=" + seatsAvailable + "/" + totalSeats;
    }
}