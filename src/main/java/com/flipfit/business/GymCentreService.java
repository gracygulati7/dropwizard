package com.flipfit.business;

import com.flipfit.bean.Slot;
import java.util.List;

public interface GymCentreService {
    List<Slot> getSlotsByCentreId(int centreId);
}