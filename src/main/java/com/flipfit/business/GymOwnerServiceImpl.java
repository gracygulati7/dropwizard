package com.flipfit.business;
import com.flipfit.bean.FlipFitGymCenter;
import com.flipfit.bean.FlipFitGymOwner;
import com.flipfit.bean.Slot;
import java.time.LocalDate;
import com.flipfit.bean.Booking;
import com.flipfit.dao.GymCentreDAO;
import com.flipfit.dao.OwnerDAO;
import com.flipfit.dao.SlotDAO;
import com.flipfit.dao.BookingDAO;
import java.util.ArrayList;
import java.util.List;

public class GymOwnerServiceImpl implements GymOwnerService {
    
    private final OwnerDAO ownerDAO = OwnerDAO.getInstance();
    private final GymCentreDAO gymCentreDAO = GymCentreDAO.getInstance();
    private final SlotDAO slotDAO = SlotDAO.getInstance();

    @Override
    public FlipFitGymOwner registerOwner(String name, String pan, String aadhaar, String gstin) {
        // Register owner in DAO
        ownerDAO.addOwner(name);
        FlipFitGymOwner registeredOwner = ownerDAO.getOrCreateOwnerByName(name);
        
        // Update the owner details
        System.out.println("\n✓ Registration Successful!");
        System.out.println("  Owner ID: " + registeredOwner.getOwnerId());
        System.out.println("  Name: " + registeredOwner.getName());
        System.out.println("  Status: PENDING APPROVAL");
        System.out.println("\n  Your gym centers will be visible to customers once approved by admin.");
        
        return registeredOwner;
    }

    @Override
    public void addCentre(int ownerId, int centerId, String gymName, String city, String state, int pincode, int capacity) {
        FlipFitGymCenter gymCenter = new FlipFitGymCenter(centerId, gymName, city, state, pincode, capacity);
        gymCenter.setOwnerId(ownerId);
        gymCentreDAO.addGymCentre(gymCenter);
        System.out.println("✓ Gym Centre '" + gymName + "' added successfully for Owner: " + ownerId);
    }

    @Override
    public List<FlipFitGymCenter> viewCentres(int ownerId) {
        List<FlipFitGymCenter> ownerCentres = new ArrayList<>();
        List<FlipFitGymCenter> allCentres = gymCentreDAO.getAllCentres();
        
        for (FlipFitGymCenter centre : allCentres) {
            if (centre.getOwnerId() == ownerId) {
                ownerCentres.add(centre);
            }
        }
        
        if (ownerCentres.isEmpty()) {
            System.out.println("No gym centres found for this owner.");
        } else {
            System.out.println("\n===== YOUR GYM CENTRES =====");
            for (FlipFitGymCenter centre : ownerCentres) {
                System.out.println(centre);
            }
        }
        return ownerCentres;
    }

    @Override
    public void addSlot(int centerId, int slotId, LocalDate date, String startTime, String endTime, int seats) {
        // 1. Get the center details
        FlipFitGymCenter center = gymCentreDAO.getGymCentreById(centerId);
        
        // 2. CHECK: Does the center exist AND is it owned by someone else?
        // Note: You might need to pass the current ownerId to this method to be 100% secure
        if (center == null) {
            System.out.println("❌ Error: Gym Centre with ID " + centerId + " does not exist.");
            return;
        }

        // 3. Proceed with adding the slot
        Slot slot = new Slot(slotId, centerId, date, startTime, endTime, seats);
        slotDAO.addSlot(slot);
        System.out.println("✓ Slot " + slotId + " added successfully for Center: " + center.getGymName());
    }

    @Override
    public void viewSlots(int ownerId, int centerId) {
        FlipFitGymCenter center = gymCentreDAO.getGymCentreById(centerId);
        
        // SECURITY CHECK: Does this center exist AND does it belong to this owner?
        if (center != null && center.getOwnerId() == ownerId) {
            List<Slot> slots = slotDAO.getSlotsByCenterId(centerId);
            if (slots.isEmpty()) {
                System.out.println("No slots found for this gym centre.");
            } else {
                System.out.println("\n===== SLOTS FOR CENTER " + centerId + " (" + center.getGymName() + ") =====");
                for (Slot slot : slots) {
                    System.out.println(slot);
                }
            }
        } else {
            System.out.println("❌ Error: You do not have permission to view slots for Center ID " + centerId + " or it does not exist.");
        }
    }

//    @Override
//    public void viewCustomers(int ownerId, int centerId) {
//        FlipFitGymCenter center = gymCentreDAO.getGymCentreById(centerId);
//
//        // SECURITY CHECK
//        if (center != null && center.getOwnerId() == ownerId) {
//            System.out.println("\n----- Customers & Bookings for Centre " + centerId + " -----");
//            // ... (your existing booking display logic) ...
//        } else {
//            System.out.println("❌ Error: You do not have permission to view customers for Center ID " + centerId);
//        }
//    }


    @Override
    public void viewCustomers(int ownerId, int centerId) {
        // 1. Verify that the center exists and actually belongs to this owner
        FlipFitGymCenter center = gymCentreDAO.getGymCentreById(centerId);

        if (center == null) {
            System.out.println("❌ Error: Gym Centre with ID " + centerId + " not found.");
            return;
        }

        if (center.getOwnerId() != ownerId) {
            System.out.println("❌ Error: You do not have permission to view bookings for this centre.");
            return;
        }

        System.out.println("\n================================================================");
        System.out.println("   BOOKINGS FOR CENTRE: " + center.getGymName() + " (ID: " + centerId + ")");
        System.out.println("================================================================");
        System.out.printf("%-10s | %-15s | %-20s | %-10s%n", "Slot ID", "Time", "Customer Name", "Status");
        System.out.println("----------------------------------------------------------------");

        // 2. Get all slots for this gym center
        List<Slot> slots = slotDAO.getSlotsByCenterId(centerId);
        boolean hasBookings = false;

        for (Slot slot : slots) {
            // 3. For each slot, get the confirmed bookings
            // We use BookingDAO here (Make sure to import it)
            List<Booking> bookings = BookingDAO.getInstance().getBookingsBySlotId(slot.getSlotId());

            for (Booking booking : bookings) {
                hasBookings = true;

                // 4. Get Customer details from UserDAO (Make sure to import it)
                var customer = com.flipfit.dao.UserDAO.getInstance().getUserById(booking.getUserId());
                String customerName = (customer != null) ? customer.getFullName() : "Unknown User";

                System.out.printf("%-10d | %-15s | %-20s | %-10s%n",
                        slot.getSlotId(),
                        slot.getStartTime(),
                        customerName,
                        booking.getStatus());
            }
        }

        if (!hasBookings) {
            System.out.println("   No active bookings found for any slots in this centre.");
        }
        System.out.println("================================================================\n");
    }

    @Override
    public void viewPayments(int ownerId) {
        System.out.println("Displaying payment history...");
    }

    @Override
    public void editDetails(int ownerId) {
        System.out.println("Gym Owner details updated.");
    }

    @Override
    public void viewProfile(int ownerId) {
        com.flipfit.dao.OwnerDAO ownerDAO = com.flipfit.dao.OwnerDAO.getInstance();
        com.flipfit.bean.FlipFitGymOwner owner = ownerDAO.getOwnerById(ownerId);
        if (owner != null) {
            System.out.println("\n===== OWNER PROFILE =====");
            System.out.println("Owner ID: " + owner.getOwnerId());
            System.out.println("Name: " + owner.getName());
            System.out.println("PAN: " + owner.getPan());
            System.out.println("Aadhaar: " + owner.getAadhaar());
            System.out.println("GSTIN: " + owner.getGstin());
            System.out.println("Validated: " + (owner.isValidated() ? "✓ YES" : "✗ NO"));
            System.out.println("Approved: " + (owner.isApproved() ? "✓ YES - Visible to Customers" : "✗ NO - Pending Admin Approval"));
            
            // Show centres count
            List<FlipFitGymCenter> centres = viewCentres(ownerId);
            System.out.println("Total Centres: " + centres.size());
        } else {
            System.out.println("Owner not found.");
        }
    }
    
    @Override
    public void registerOwner(String name, String email, String password, String pan, String aadhaar, String gstin) {
        // Step 1: Create entry in 'users' table
        ownerDAO.addOwner(name, email, password); 

        // Step 2: Fetch the ID from 'users' table immediately (No JOIN needed)
        int userId = ownerDAO.getUserIdByEmail(email);
        
        if (userId != -1) {
            // Step 3: Create entry in 'owner' table using that ID
            ownerDAO.addOwnerDetails(userId, pan, aadhaar, gstin);
            System.out.println("✓ Account created and professional details linked for: " + name);
        } else {
            System.out.println("✗ Critical Error: Could not retrieve user ID after registration.");
        }
    }
}
