package com.flipfit.bean;

// TODO: Auto-generated Javadoc
/**
 * The Class FlipFitGymOwner.
 *
 * Represents a gym owner with KYC details and approval state.
 *
 * @author JEDI-DELTA
 * @ClassName  "FlipFitGymOwner"
 */
public class FlipFitGymOwner {

    private int ownerId;
    private String name;
    private String pan;
    private String aadhaar;
    private String gstin;
    private boolean isValidated;
    private boolean isApproved;

    /**
     * Instantiates a new flip fit gym owner.
     *
     * @param ownerId the owner id
     * @param name the owner's name
     * @param pan the PAN identifier
     * @param aadhaar the Aadhaar identifier
     * @param gstin the GSTIN identifier
     */
    public FlipFitGymOwner(int ownerId, String name, String pan, String aadhaar, String gstin) {
        this.ownerId = ownerId;
        this.name = name;
        this.pan = pan;
        this.aadhaar = aadhaar;
        this.gstin = gstin;
        this.isValidated = false;
        this.isApproved = false; 
    }

    /**
     * Gets the owner id.
     *
     * @return the owner id
     */
    public int getOwnerId() {
        return ownerId;
    }

    /**
     * Gets the owner's name.
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the PAN.
     *
     * @return the pan
     */
    public String getPan() {
        return pan;
    }

    /**
     * Gets the Aadhaar.
     *
     * @return the aadhaar
     */
    public String getAadhaar() {
        return aadhaar;
    }

    /**
     * Gets the GSTIN.
     *
     * @return the gstin
     */
    public String getGstin() {
        return gstin;
    }

    /**
     * Checks if the owner is validated.
     *
     * @return true, if validated
     */
    public boolean isValidated() {
        return isValidated;
    }

    /**
     * Sets the validated flag.
     *
     * @param validated the new validated status
     */
    public void setValidated(boolean validated) {
        isValidated = validated;
    }

    /**
     * Checks if the owner is approved.
     *
     * @return true, if approved
     */
    public boolean isApproved() {
        return isApproved;
    }

    /**
     * Sets the approved flag.
     *
     * @param approved the new approved status
     */
    public void setApproved(boolean approved) {
        isApproved = approved;
    }

    /**
     * Returns a string representation of the gym owner.
     *
     * @return the string
     */
    @Override
    public String toString() {
        return "GymOwner [OwnerId=" + ownerId + ", Name=" + name + ", Pan=" + pan + 
               ", Aadhaar=" + aadhaar + ", GSTIN=" + gstin + 
               ", IsValidated=" + isValidated + ", IsApproved=" + isApproved + "]";
    }
}