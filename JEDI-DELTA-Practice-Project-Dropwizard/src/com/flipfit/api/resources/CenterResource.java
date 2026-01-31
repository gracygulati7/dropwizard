package com.flipfit.api.resources;

import com.flipfit.api.dto.ApiResponse;
import com.flipfit.bean.FlipFitGymCenter;
import com.flipfit.bean.Slot;
import com.flipfit.dao.GymCentreDAO;
import com.flipfit.dao.SlotDAO;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.time.LocalDate;
import java.util.List;

@Path("/centers")
@Produces(MediaType.APPLICATION_JSON)
public class CenterResource {

    private final GymCentreDAO gymCentreDAO = GymCentreDAO.getInstance();
    private final SlotDAO slotDAO = SlotDAO.getInstance();

    @GET
    public Response listCenters() {
        List<FlipFitGymCenter> centers = gymCentreDAO.getAllCentres();
        return Response.ok(ApiResponse.ok("Centers fetched", centers)).build();
    }

    @GET
    @Path("/{centerId}")
    public Response getCenter(@PathParam("centerId") int centerId) {
        FlipFitGymCenter center = gymCentreDAO.getGymCentreById(centerId);
        if (center == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(ApiResponse.fail("Center not found"))
                    .build();
        }
        return Response.ok(ApiResponse.ok("Center fetched", center)).build();
    }

    @GET
    @Path("/{centerId}/slots")
    public Response listSlots(@PathParam("centerId") int centerId) {
        List<Slot> slots = slotDAO.getSlotsByCenterId(centerId);
        return Response.ok(ApiResponse.ok("Slots fetched", slots)).build();
    }

    @GET
    @Path("/{centerId}/slots/available")
    public Response listAvailableSlots(@PathParam("centerId") int centerId, @QueryParam("date") String date) {
        LocalDate slotDate = date == null ? LocalDate.now() : LocalDate.parse(date);
        List<Slot> slots = slotDAO.getAvailableSlotsByDateAndCenter(centerId, slotDate);
        return Response.ok(ApiResponse.ok("Available slots fetched", slots)).build();
    }
}
