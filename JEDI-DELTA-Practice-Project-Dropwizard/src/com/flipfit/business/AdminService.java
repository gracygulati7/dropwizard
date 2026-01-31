package com.flipfit.business;
import com.flipfit.bean.FlipFitCustomer;
import com.flipfit.bean.FlipFitGymOwner;

public interface AdminService {

    // From diagram
    void login();
    void validateOwner(int ownerId);
    void deleteOwner(int ownerId);
    void viewFFCustomers();

    // REQUIRED by assignment
    void addGymCenter(int centerId, String gymName, String city, String state, int pincode, int capacity);
    void viewGymCenters();

    void addSlotInfo(int centerId, int slotId, String startTime,String endTime, int seats);
    void viewSlots(int centerId);

    // Utility: lookup customer by id
    FlipFitCustomer getCustomerById(int userId);

    // New: Owner management for approval workflow
    void viewAllGymOwners();
    FlipFitGymOwner getOwnerById(int ownerId);
    void approveOwner(int ownerId);
}
