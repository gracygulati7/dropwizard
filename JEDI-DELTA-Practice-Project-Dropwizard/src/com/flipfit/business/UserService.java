package com.flipfit.business;

import com.flipfit.bean.Slot;
import java.util.List;

public interface UserService {

    void viewProfile(int userId);

    void editProfile(int userId);

    List<Slot> findAvailableSlots(int centreId);
}