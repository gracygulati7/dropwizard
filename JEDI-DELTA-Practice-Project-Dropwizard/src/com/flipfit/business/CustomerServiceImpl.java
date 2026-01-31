package com.flipfit.business;

import com.flipfit.dao.CustomerDAO;
import com.flipfit.bean.FlipFitCustomer;
import java.util.Scanner;

public class CustomerServiceImpl implements CustomerService {
	private final CustomerDAO customerDAO = CustomerDAO.getInstance();

    @Override
    public boolean makePayment(int userId, int amount) {

        FlipFitCustomer customer = customerDAO.getCustomerById(userId);

        if (customer == null) {
            System.out.println("❌ Customer not found.");
            return false;
        }

        Scanner sc = new Scanner(System.in);
        
        System.out.println("\n===== PAYMENT =====");
        System.out.println("Amount to pay: ₹" + amount);
        System.out.println("Select Payment Method:");
        System.out.println("1. Card");
        System.out.println("2. UPI");

        int choice = sc.nextInt();
        sc.nextLine(); // consume newline

        String paymentInfo;

        switch (choice) {
            case 1:
                System.out.print("Enter Card Number (last 4 digits): ");
                paymentInfo = sc.nextLine();
                customerDAO.updatePaymentDetails(userId, 1, paymentInfo);
                break;

            case 2:
                System.out.print("Enter UPI ID: ");
                paymentInfo = sc.nextLine();
                customerDAO.updatePaymentDetails(userId, 2, paymentInfo);
                break;

            default:
                System.out.println("❌ Invalid payment option.");
                return false;
        }

        System.out.println("✅ Payment of ₹" + amount + " successful!");
        return true;
    }

    @Override
    public void viewPaymentInfo(int userId) {

        FlipFitCustomer customer =
                CustomerDAO.getInstance().getCustomerById(userId);

        if (customer == null) {
            System.out.println("❌ Customer not found.");
            return;
        }

        System.out.println("\n===== PAYMENT DETAILS =====");

        if (customer.getPaymentType() == 0) {
            System.out.println("No payment method saved.");
            return;
        }

        String paymentMethod =
                (customer.getPaymentType() == 1) ? "Card" : "UPI";

        System.out.println("Payment Method: " + paymentMethod);

        if (customer.getPaymentType() == 1) {
            // Mask card number
            String info = customer.getPaymentInfo();
            System.out.println("Card: **** **** **** " + info);
        } else {
            System.out.println("UPI ID: " + customer.getPaymentInfo());
        }
    }
    
    @Override
    public void viewBookedSlots(int userId) {
        System.out.println("Displaying booked slots for User: " + userId);
    }

    @Override
    public boolean checkBookingConflicts(int userId, int slotId) {
        return false; 
    }

//    @Override
//    public boolean makePayment(int userId, int amount) {
//        System.out.println("Payment of " + amount + " successful for user " + userId);
//        return true;
//    }

//    @Override
//    public void editDetails(int userId) {
//        System.out.println("Updating profile for " + userId);
//    }

    @Override
    public java.util.List<Object> viewCentres(String city) {
        return null; 
    }
}