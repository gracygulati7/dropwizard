package com.flipfit.business;

import com.flipfit.bean.FlipFitGymCenter;
import com.flipfit.bean.Slot;
import com.flipfit.dao.GymCentreDAO;
import com.flipfit.dao.SlotDAO;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class NotificationServiceImpl implements NotificationService {
    
    private static NotificationServiceImpl instance;
    private Map<Integer, List<String>> userNotifications; // userId -> List of notifications
    private DateTimeFormatter timeFormatter;
    private SlotDAO slotDAO = SlotDAO.getInstance();
    private GymCentreDAO gymCentreDAO = GymCentreDAO.getInstance();
    
    private NotificationServiceImpl() {
        this.userNotifications = new HashMap<>();
        this.timeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    }
    
    public static synchronized NotificationServiceImpl getInstance() {
        if (instance == null) {
            instance = new NotificationServiceImpl();
        }
        return instance;
    }
    
    private void addNotificationForUser(int userId, String message) {
        userNotifications.computeIfAbsent(userId, k -> new ArrayList<>()).add(message);
    }
    
    @Override
    public void sendBookingConfirmation(int userId, int slotId, int centerId) {
        Slot slot = slotDAO.getSlotById(userId, slotId,centerId);
        FlipFitGymCenter center = gymCentreDAO.getGymCentreById(centerId);
        
        String centerName = (center != null) ? center.getGymName() : "Unknown Center";
        String slotTime = (slot != null) ? slot.getStartTime() + " - " + slot.getEndTime() : "Unknown Time";
        
        String message = "[" + LocalDateTime.now().format(timeFormatter) + "] ✓ BOOKING CONFIRMED\n" +
                        "   Slot ID: " + slotId + "\n" +
                        "   Center: " + centerName + " (ID: " + centerId + ")\n" +
                        "   Time: " + slotTime;
        
        System.out.println("\n" + message);
        addNotificationForUser(userId, message);
    }

    @Override
    public void sendWaitlistPromotion(int userId, int slotId, int centerId) {
        Slot slot = slotDAO.getSlotById(userId, slotId,centerId);
        FlipFitGymCenter center = gymCentreDAO.getGymCentreById(centerId);
        
        String centerName = (center != null) ? center.getGymName() : "Unknown Center";
        String slotTime = (slot != null) ? slot.getStartTime() + " - " + slot.getEndTime() : "Unknown Time";
        
        String message = "[" + LocalDateTime.now().format(timeFormatter) + "] ⬆️  PROMOTED FROM WAITLIST\n" +
                        "   Slot ID: " + slotId + "\n" +
                        "   Center: " + centerName + " (ID: " + centerId + ")\n" +
                        "   Time: " + slotTime;
        
        System.out.println("\n" + message);
        addNotificationForUser(userId, message);
    }
    
    @Override
    public void sendCancellationNotification(int userId, int slotId, int centerId) {
        Slot slot = slotDAO.getSlotById(userId, slotId, centerId);
        String slotTime = (slot != null) ? slot.getStartTime() + " - " + slot.getEndTime() : "Unknown Time";
        
        String message = "[" + LocalDateTime.now().format(timeFormatter) + "] ✗ BOOKING CANCELLED\n" +
                        "   Slot ID: " + slotId + "\n" +
                        "   Time: " + slotTime;
        
        System.out.println("\n" + message);
        addNotificationForUser(userId, message);
    }
    
    @Override
    public void sendConflictWarning(int userId, String message) {
        String fullMessage = "[" + LocalDateTime.now().format(timeFormatter) + "] ⚠️  CONFLICT WARNING\n" +
                           "   Details: " + message;
        
        System.out.println("\n" + fullMessage);
        addNotificationForUser(userId, fullMessage);
    }
    
    @Override
    public void sendSlotFullNotification(int userId, int slotId, int centerId) {
        Slot slot = slotDAO.getSlotById(userId, slotId,centerId);
        FlipFitGymCenter center = gymCentreDAO.getGymCentreById(centerId);
        
        String centerName = (center != null) ? center.getGymName() : "Unknown Center";
        String slotTime = (slot != null) ? slot.getStartTime() + " - " + slot.getEndTime() : "Unknown Time";
        
        String message = "[" + LocalDateTime.now().format(timeFormatter) + "] 📋 ADDED TO WAITLIST\n" +
                        "   Slot ID: " + slotId + "\n" +
                        "   Center: " + centerName + " (ID: " + centerId + ")\n" +
                        "   Time: " + slotTime + "\n" +
                        "   Status: Slot is full, you're in the queue";
        
        System.out.println("\n" + message);
        addNotificationForUser(userId, message);
    }
    
    // Get notifications for specific user
    public List<String> getUserNotifications(int userId) {
        return new ArrayList<>(userNotifications.getOrDefault(userId, new ArrayList<>()));
    }
    
    // Get all notifications (for admin/testing purposes)
    public List<String> getAllNotifications() {
        List<String> allNotifs = new ArrayList<>();
        for (List<String> userNotifs : userNotifications.values()) {
            allNotifs.addAll(userNotifs);
        }
        return allNotifs;
    }
    
    public void printUserNotifications(int userId) {
        System.out.println("\n╔════════════════════════════════════════╗");
        System.out.println("║        YOUR NOTIFICATIONS              ║");
        System.out.println("╚════════════════════════════════════════╝");
        
        List<String> notifications = getUserNotifications(userId);
        
        if (notifications.isEmpty()) {
            System.out.println("\n📭 No notifications yet.\n");
        } else {
            int count = 1;
            for (String notification : notifications) {
                System.out.println("\n[" + count + "] " + notification);
                System.out.println("─────────────────────────────────────");
                count++;
            }
            System.out.println("\nTotal Notifications: " + notifications.size() + "\n");
        }
    }
    
    public void clearUserNotifications(int userId) {
        userNotifications.remove(userId);
    }
    
    public void clearAllNotifications() {
        userNotifications.clear();
    }
}
