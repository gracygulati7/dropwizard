package com.flipfit.bean;

// TODO: Auto-generated Javadoc
/**
 * The Class FlipFitCustomer.
 *
 * @author JEDI-DELTA
 * @ClassName  "FlipFitCustomer"
 */
public class FlipFitCustomer extends FlipFitUser {

    // From class diagram
    private int paymentType;     // e.g. 1 = Card, 2 = UPI
    private String paymentInfo;  // card/upi details
    private String contact;      // phone/email contact

    /**
     * Instantiates a new flip fit customer.
     */
    public FlipFitCustomer() {
    	super();
    	this.role="CUSTOMER";
    	this.contact="N/A";
    }
    
    /**
     * Instantiates a new flip fit customer with id and name.
     *
     * @param userId the user ID
     * @param fullName the full name
     */
    public FlipFitCustomer(int userId, String fullName) {
        this.userId = userId;
        this.fullName = fullName;
        this.role = "CUSTOMER";
        this.contact = "N/A";
    }

    // --- getters & setters ---

    /**
     * Gets the user id.
     *
     * @return the user id
     */
    public int getUserId() {
        return userId;
    }

    /**
     * Sets the user id.
     *
     * @param userId the new user id
     */
    public void setUserId(int userId) {
        this.userId = userId;
    }
    
    /**
     * Gets the payment type.
     *
     * @return the payment type
     */
    public int getPaymentType() {
        return paymentType;
    }

    /**
     * Sets the payment type.
     *
     * @param paymentType the new payment type
     */
    public void setPaymentType(int paymentType) {
        this.paymentType = paymentType;
    }

    /**
     * Gets the payment info.
     *
     * @return the payment info
     */
    public String getPaymentInfo() {
        return paymentInfo;
    }

    /**
     * Sets the payment info.
     *
     * @param paymentInfo the new payment info
     */
    public void setPaymentInfo(String paymentInfo) {
        this.paymentInfo = paymentInfo;
    }

    /**
     * Gets the contact.
     *
     * @return the contact
     */
    public String getContact() {
        return contact;
    }

    /**
     * Sets the contact.
     *
     * @param contact the new contact
     */
    public void setContact(String contact) {
        this.contact = contact;
    }

    /**
     * Returns a string representation of the customer.
     *
     * @return the string
     */
    @Override
    public String toString() {
    	 return "Customer [Id=" + userId +
                 ", Name=" + fullName +
                 ", Contact=" + contact +
                 ", PaymentType=" + paymentType + "]";
      }
  }

