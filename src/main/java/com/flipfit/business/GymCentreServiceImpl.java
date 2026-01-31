package com.flipfit.business;

import com.flipfit.bean.Slot;
import com.flipfit.dao.SlotDAO;
import java.util.List;

public class GymCentreServiceImpl implements GymCentreService {

    private final SlotDAO slotDAO = SlotDAO.getInstance();

    @Override
    public List<Slot> getSlotsByCentreId(int centreId) {
        return slotDAO.getSlotsByCenterId(centreId);
    }
}