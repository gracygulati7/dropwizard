package com.flipfit.business;

public interface NotificationService{
    public void sendBookingConfirmation(int userId, int slotId,int centerId);

    public void sendWaitlistPromotion(int userId, int slotId,int centerId);
    
    public void sendCancellationNotification(int userId, int slotId, int centerId);
    
    public void sendConflictWarning(int userId, String message);
    
    public void sendSlotFullNotification(int userId, int slotId,int centerId);
    
}