package com.flipfit.api.dto;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

public class CreateBookingRequest {
    @NotNull
    @Min(1)
    private Integer userId;

    @NotNull
    @Min(1)
    private Integer slotId;

    @NotNull
    @Min(1)
    private Integer centerId;

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getSlotId() {
        return slotId;
    }

    public void setSlotId(Integer slotId) {
        this.slotId = slotId;
    }

    public Integer getCenterId() {
        return centerId;
    }

    public void setCenterId(Integer centerId) {
        this.centerId = centerId;
    }
}
