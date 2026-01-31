package com.flipfit.business;

import java.util.List;
import java.util.ArrayList;
import java.util.Scanner;
import com.flipfit.bean.Slot;
import com.flipfit.dao.CustomerDAO;
import com.flipfit.dao.GymCentreDAO;
import com.flipfit.dao.SlotDAO;
import com.flipfit.dao.UserDAO;
import com.flipfit.bean.FlipFitCustomer;

public class UserServiceImpl implements UserService {

    private final SlotDAO slotDAO = SlotDAO.getInstance();
    private final CustomerDAO customerDAO = CustomerDAO.getInstance();
    private final GymCentreDAO gymCentreDAO = GymCentreDAO.getInstance();
    private final UserDAO userDAO = UserDAO.getInstance();

    @Override
    public List<Slot> findAvailableSlots(int centreId) {
        List<Slot> allSlots = slotDAO.getSlotsByCenterId(centreId);
        List<Slot> availableSlots = new ArrayList<>();

        for (Slot slot : allSlots) {
            if (slot.getDate() != null &&
                slot.getDate().isAfter(java.time.LocalDate.now().minusDays(1)) && // today or future
                !slot.isExpired() &&
                slot.getSeatsAvailable() > 0) {
                availableSlots.add(slot);
            }
        }

        // sort by date and start time
        availableSlots.sort((s1, s2) -> {
            int cmp = s1.getDate().compareTo(s2.getDate());
            if (cmp == 0) {
                return s1.getStartTime().compareTo(s2.getStartTime());
            }
            return cmp;
        });

        return availableSlots;
    }

    @Override
    public void viewProfile(int userId) {
        // CHANGED: Use userDAO to look in 'users' table (works for Admin/Owner/Customer)
        FlipFitCustomer user = userDAO.getUserById(userId); 
        
        if (user != null) {
            System.out.println("\n========== PROFILE ==========");
            System.out.println("User ID : " + user.getUserId());
            System.out.println("Name    : " + user.getFullName());
            System.out.println("Email   : " + user.getEmail());
            System.out.println("Role    : " + user.getRole());
            System.out.println("=============================");
        } else {
            System.out.println("✗ User not found with ID: " + userId);
        }
    }

    @Override
    public void editProfile(int userId) {
        // CHANGED: Use userDAO
        FlipFitCustomer user = userDAO.getUserById(userId);

        if (user == null) {
            System.out.println("✗ User not found");
            return;
        }

        Scanner sc = new Scanner(System.in);
        System.out.println("Editing profile for: " + user.getFullName());
        
        System.out.print("New Full Name (Enter to skip): ");
        String newName = sc.nextLine();
        if (!newName.trim().isEmpty()) user.setFullName(newName.trim());

        System.out.print("New Email (Enter to skip): ");
        String newEmail = sc.nextLine();
        if (!newEmail.trim().isEmpty()) user.setEmail(newEmail.trim());

        // Update the 'users' table
        userDAO.updateBaseUser(user);
        
        // If the user is a customer, update the 'customers' table too
        if ("CUSTOMER".equalsIgnoreCase(user.getRole())) {
            customerDAO.updateCustomer(user);
        }
        
        System.out.println("✅ Profile updated successfully!");
    }
}