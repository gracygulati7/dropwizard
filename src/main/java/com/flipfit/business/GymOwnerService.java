package com.flipfit.business;
import com.flipfit.bean.FlipFitGymCenter;
import com.flipfit.bean.FlipFitGymOwner;
import java.util.List;

public interface GymOwnerService {
    // Registration
    FlipFitGymOwner registerOwner(String name, String pan, String aadhaar, String gstin);
    
    void addCentre(int ownerId, int centerId, String gymName, String city, String state, int pincode, int capacity);
    List<FlipFitGymCenter> viewCentres(int ownerId);
    void addSlot(int centerId, int slotId, java.time.LocalDate date, String startTime,String endTime, int seats);
    void viewSlots(int ownerId, int centerId); 
    void viewCustomers(int ownerId, int centerId);
    void viewPayments(int ownerId);
    void editDetails(int ownerId);
    void viewProfile(int ownerId);
    void registerOwner(String name, String email, String password, String pan, String aadhaar, String gstin);
}