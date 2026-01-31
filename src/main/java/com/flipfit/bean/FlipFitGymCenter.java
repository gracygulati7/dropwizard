package com.flipfit.bean;

import java.util.ArrayList;
import java.util.List;

// TODO: Auto-generated Javadoc
/**
 * The Class FlipFitGymCenter.
 *
 * Represents a gym center with its basic metadata and available slots.
 *
 * @author JEDI-DELTA
 * @ClassName  "FlipFitGymCenter"
 */
public class FlipFitGymCenter {

    private int centerId;
    private String gymName;
    private int ownerId;
    private int capacity;
    private boolean approved;
    private String city;
    private String state;
    private int pincode;

    private List<Slot> slots = new ArrayList<>();

    /**
     * Instantiates a new flip fit gym center.
     *
     * @param centerId the center id
     * @param gymName the gym name
     * @param city the city
     * @param state the state
     * @param pincode the pincode
     * @param capacity the capacity
     */
    public FlipFitGymCenter(int centerId, String gymName, String city, String state, int pincode, int capacity) {
        this.centerId = centerId;
        this.gymName = gymName;
        this.city = city;
        this.state = state;
        this.pincode = pincode;
        this.capacity = capacity;
        this.approved = false;
    }

    /**
     * Gets the gym id (alias for center id).
     *
     * @return the gym id
     */
    public int getGymId() { return centerId; }

    /**
     * Gets the gym name.
     *
     * @return the gym name
     */
    public String getGymName() { return gymName; }

    /**
     * Gets the location as a combined string of city and state.
     *
     * @return the location
     */
    public String getLocation() { return city + ", " + state; }

    /**
     * Gets the center id.
     *
     * @return the center id
     */
    public int getCenterId() { return centerId; }

    /**
     * Gets the owner id.
     *
     * @return the owner id
     */
    public int getOwnerId() { return ownerId; }

    /**
     * Gets the capacity of the center.
     *
     * @return the capacity
     */
    public int getCapacity() { return capacity; }

    /**
     * Checks if the center is approved.
     *
     * @return true, if approved
     */
    public boolean isApproved() { return approved; }

    /**
     * Gets the city.
     *
     * @return the city
     */
    public String getCity() { return city; }

    /**
     * Gets the state.
     *
     * @return the state
     */
    public String getState() { return state; }

    /**
     * Gets the pincode.
     *
     * @return the pincode
     */
    public int getPincode() { return pincode; }

    /**
     * Gets the list of slots for this center.
     *
     * @return the slots
     */
    public List<Slot> getSlots() { return slots; }

    /**
     * Adds a slot to this center.
     *
     * @param slot the slot to add
     */
    public void addSlot(Slot slot) { slots.add(slot); }

    /**
     * Sets the approved flag for this center.
     *
     * @param approved the new approved status
     */
    public void setApproved(boolean approved) { this.approved = approved; }

    /**
     * Sets the owner id for this center.
     *
     * @param ownerId the new owner id
     */
    public void setOwnerId(int ownerId) { this.ownerId = ownerId; }

    /**
     * Returns a string representation of the gym center.
     *
     * @return the string
     */
    @Override
    public String toString() {
        return "CenterId=" + centerId +
               ", GymName=" + gymName +
               ", OwnerId=" + ownerId +
               ", City=" + city +
               ", State=" + state +
               ", Pincode=" + pincode +
               ", Capacity=" + capacity +
               ", Approved=" + approved;
    }
}