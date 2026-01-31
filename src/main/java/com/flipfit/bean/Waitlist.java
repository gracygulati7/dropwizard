package com.flipfit.bean;

import java.util.Queue;
import java.util.LinkedList;

// TODO: Auto-generated Javadoc
/**
 * The Class Waitlist.
 *
 * Represents a waitlist for a particular slot where customers are queued in FIFO order.
 *
 * @author JEDI-DELTA
 * @ClassName  "Waitlist"
 */
public class Waitlist {

    private int waitlistId;
    private int slotId;
    private Queue<Integer> customerQueue = new LinkedList<>(); 

    /**
     * Instantiates a new waitlist for a slot.
     *
     * @param waitlistId the waitlist id
     * @param slotId the slot id this waitlist belongs to
     */
    public Waitlist(int waitlistId, int slotId) {
        this.waitlistId = waitlistId;
        this.slotId = slotId;
    }

    /**
     * Adds a customer to the waitlist queue.
     *
     * @param userId the user id to add
     */
    public void addCustomer(int userId) {
        customerQueue.add(userId);
    }

    /**
     * Removes and returns the next customer in line, or null if empty.
     *
     * @return the next user id or null
     */
    public Integer getNextInLine() {
        return customerQueue.poll();
    }

    /**
     * Checks whether the waitlist is empty.
     *
     * @return true if empty
     */
    public boolean isEmpty() {
        return customerQueue.isEmpty();
    }

    /**
     * Gets the waitlist id.
     *
     * @return the waitlist id
     */
    public int getWaitlistId() {
        return waitlistId;
    }

    /**
     * Sets the waitlist id.
     *
     * @param waitlistId the new waitlist id
     */
    public void setWaitlistId(int waitlistId) {
        this.waitlistId = waitlistId;
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
     * Gets the underlying customer queue.
     *
     * @return the customer queue
     */
    public Queue<Integer> getCustomerQueue() {
        return customerQueue;
    }
}