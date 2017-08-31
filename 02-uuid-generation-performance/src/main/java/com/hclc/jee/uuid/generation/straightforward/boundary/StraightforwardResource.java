package com.hclc.jee.uuid.generation.straightforward.boundary;

import com.hclc.jee.uuid.generation.straightforward.control.StraightforwardGenerator;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

@Stateless
@Path(value = "straightforward")
public class StraightforwardResource {

    @Inject
    StraightforwardGenerator straightforwardGenerator;

    @GET
    @Produces(APPLICATION_JSON)
    public Response straightforward() {
        return Response.ok(straightforwardGenerator.getNextBatch()).build();
    }
}
