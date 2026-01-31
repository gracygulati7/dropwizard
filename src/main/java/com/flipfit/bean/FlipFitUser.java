package com.flipfit.bean;

// TODO: Auto-generated Javadoc
/**
 * The Class FlipFitUser.
 *
 * Represents a generic user in the FlipFit system.
 *
 * @author JEDI-DELTA
 * @ClassName  "FlipFitUser"
 */
public class FlipFitUser {

    protected int userId;
    protected String fullName;
    protected String email;
    protected String password;
    protected long phoneNumber;
    protected String city;
    protected int pincode;
    protected String role;

    /**
     * Instantiates a new FlipFit user.
     */
    public FlipFitUser() {}

    /**
     * Instantiates a new FlipFit user with id and name.
     *
     * @param userId the user id
     * @param fullName the full name
     */
    public FlipFitUser(int userId, String fullName) {
        this.userId = userId;
        this.fullName = fullName;
    }

    // Getters and Setters

    /**
     * Gets the user id.
     *
     * @return the user id
     */
    public int getUserId() {
        return userId;
    }

    /**
     * Gets the full name.
     *
     * @return the full name
     */
    public String getFullName() {
        return fullName;
    }

    /**
     * Sets the full name.
     *
     * @param fullName the new full name
     */
    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    /**
     * Gets the email.
     *
     * @return the email
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets the email.
     *
     * @param email the new email
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Gets the phone number.
     *
     * @return the phone number
     */
    public long getPhone() {
        return phoneNumber;
    }

    /**
     * Sets the phone number.
     *
     * @param phoneNumber the new phone number
     */
    public void setPhone(long phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    /**
     * Gets the city.
     *
     * @return the city
     */
    public String getCity() {
        return city;
    }

    /**
     * Sets the city.
     *
     * @param city the new city
     */
    public void setCity(String city) {
        this.city = city;
    }

    /**
     * Gets the pincode.
     *
     * @return the pincode
     */
    public int getPincode() {
        return pincode;
    }

    /**
     * Sets the pincode.
     *
     * @param pincode the new pincode
     */
    public void setPincode(int pincode) {
        this.pincode = pincode;
    }

    /**
     * Gets the role.
     *
     * @return the role
     */
    public String getRole() {
        return role;
    }

    /**
     * Sets the role.
     *
     * @param role the new role
     */
    public void setRole(String role) {
        this.role = role;
    }
}