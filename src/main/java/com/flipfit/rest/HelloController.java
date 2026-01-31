package com.flipfit.rest;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/hello")
@Produces(MediaType.TEXT_PLAIN)
public class HelloController {

    @GET
    @Path("/fetch")
    public String fetch() {
        return "This is my first service";
    }

    @POST
    @Path("/create")
    public String create() {
        return "Created";
    }

    @PUT
    @Path("/update")
    public String update() {
        return "Updated";
    }

    @DELETE
    @Path("/delete")
    public String delete() {
        return "Deleted";
    }
}
