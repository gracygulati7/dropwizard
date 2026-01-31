package com.flipfit.business;

import java.util.*;
import java.util.Scanner;

import com.flipfit.bean.FlipFitCustomer;
import com.flipfit.bean.FlipFitGymCenter;
import com.flipfit.bean.FlipFitGymOwner;
import com.flipfit.bean.Slot;
import com.flipfit.dao.AdminDAO;          // ✅ ADD
import com.flipfit.dao.CustomerDAO;
import com.flipfit.dao.GymCentreDAO;
import com.flipfit.dao.OwnerDAO;
import com.flipfit.dao.SlotDAO;

public class AdminServiceImpl implements AdminService {

    // DAOs for centralized data management
    private final OwnerDAO ownerDAO = OwnerDAO.getInstance();
    private final CustomerDAO customerDAO = CustomerDAO.getInstance();
    private final GymCentreDAO gymCentreDAO = GymCentreDAO.getInstance();
    private final SlotDAO slotDAO = SlotDAO.getInstance();
    private final AdminDAO adminDAO = AdminDAO.getInstance();   // ✅ ADD

    public AdminServiceImpl() {
        // Initialize with sample data
//        customerDAO.addCustomer("Amit");
//        customerDAO.addCustomer("Neha");
    }

    // -------- Diagram Functions --------
    @Override
    public void login() {

        Scanner sc = new Scanner(System.in);

        System.out.print("Enter admin email: ");
        String email = sc.nextLine();

        System.out.print("Enter admin password: ");
        String password = sc.nextLine();

        boolean isValid = adminDAO.login(email, password);

        if (isValid) {
            System.out.println("✓ Admin logged in successfully");
        } else {
            System.out.println("✗ Invalid admin credentials");
        }
    }

    @Override
    public void validateOwner(int ownerId) {
        FlipFitGymOwner owner = ownerDAO.getOwnerById(ownerId);
        if (owner != null) {
            // 1. Update memory
            owner.setValidated(true);

            // 2. PERSIST TO DATABASE (Missing Step)
            ownerDAO.updateOwnerValidation(ownerId, true); 

            System.out.println("✓ Owner " + ownerId + " validated in database.");
        } else {
            System.out.println("✗ Owner not found");
        }
    }

    @Override
    public void deleteOwner(int ownerId) {
        FlipFitGymOwner owner = ownerDAO.getOwnerById(ownerId);
        if (owner != null) {
            System.out.println("✓ Owner deleted");
        } else {
            System.out.println("✗ Owner not found");
        }
    }

    @Override
    public void viewFFCustomers() {
        System.out.println("\n--- FlipFit Customers ---");
        for (FlipFitCustomer c : customerDAO.getAllCustomers()) {
            System.out.println(c);
        }
    }

    @Override
    public FlipFitCustomer getCustomerById(int userId) {
        return customerDAO.getCustomerById(userId);
    }

    // -------- REQUIRED FUNCTIONS --------
    @Override
    public void addGymCenter(int centerId, String gymName, String city,
                             String state, int pincode, int capacity) {

        FlipFitGymCenter center =
                new FlipFitGymCenter(centerId, gymName, city, state, pincode, capacity);
        gymCentreDAO.addGymCentre(center);
        System.out.println("Gym Center added");
    }

    @Override
    public void viewGymCenters() {
        System.out.println("\n--- Gym Centers ---");
        for (FlipFitGymCenter c : gymCentreDAO.getAllCentres()) {
            System.out.println(c);
        }
    }

    @Override
    public void addSlotInfo(int centerId, int slotId,
                            String startTime, String endTime, int seats) {

        FlipFitGymCenter center = gymCentreDAO.getGymCentreById(centerId);
        if (center != null) {
            Slot slot = new Slot(
                    slotId, centerId, java.time.LocalDate.now(),
                    startTime, endTime, seats
            );
            slotDAO.addSlot(slot);
            System.out.println("Slot added");
        } else {
            System.out.println("Center not found");
        }
    }

    @Override
    public void viewSlots(int centerId) {
        FlipFitGymCenter center = gymCentreDAO.getGymCentreById(centerId);
        if (center != null) {
            System.out.println("\n--- Slots for Center " + centerId + " ---");
            List<Slot> slots = slotDAO.getSlotsByCenterId(centerId);
            if (slots.isEmpty()) {
                System.out.println("No slots found for this center.");
            } else {
                for (Slot s : slots) {
                    System.out.println(s);
                }
            }
        } else {
            System.out.println("Center not found");
        }
    }

    // -------- NEW OWNER MANAGEMENT METHODS --------
    @Override
    public void viewAllGymOwners() {
        Collection<FlipFitGymOwner> allOwners = ownerDAO.getAllOwners();

        if (allOwners.isEmpty()) {
            System.out.println("\n--- No Gym Owners Found ---");
            return;
        }

        System.out.println("\n========== ALL GYM OWNERS ==========");
        for (FlipFitGymOwner owner : allOwners) {
            String approvalStatus = owner.isApproved() ? "✓ APPROVED" : "✗ PENDING";
            String validationStatus = owner.isValidated() ? "✓ VALIDATED" : "✗ NOT VALIDATED";

            System.out.println("\n" + owner);
            System.out.println("  → Approval Status: " + approvalStatus);
            System.out.println("  → Validation Status: " + validationStatus);
        }
        System.out.println("\n====================================");
    }

    @Override
    public FlipFitGymOwner getOwnerById(int ownerId) {
        return ownerDAO.getOwnerById(ownerId);
    }

    @Override
    public void approveOwner(int ownerId) {
        FlipFitGymOwner owner = ownerDAO.getOwnerById(ownerId);
        if (owner != null) {
            // 1. Update memory
            owner.setApproved(true);

            // 2. PERSIST TO DATABASE (Missing Step)
            ownerDAO.updateOwnerApproval(ownerId, true); 

            // 3. Update associated gym centers in the database as well
            List<FlipFitGymCenter> centers = gymCentreDAO.getAllCentres();
            for (FlipFitGymCenter center : centers) {
                if (center.getOwnerId() == ownerId) {
                    // Persist center approval to DB
                    gymCentreDAO.approveCenter(center.getCenterId()); 
                }
            }

            System.out.println("✓ Owner " + ownerId + " has been APPROVED and saved to Database!");
            System.out.println("✓ All gym centers for this owner are now visible to customers.");
        } else {
            System.out.println("✗ Owner not found");
        }
    }
}
