package com.hclc.jee.misleading.asynchronous.stoppable.asynchronous.nonblocking.boundary;

import com.hclc.jee.misleading.asynchronous.stoppable.asynchronous.nonblocking.control.NonblockingGenerator;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

@Stateless
@Path(value = "nonblocking")
public class NonblockingResource {

    @Inject
    NonblockingGenerator generator;

    @GET
    @Produces(APPLICATION_JSON)
    public Response uniqueIds() throws InterruptedException {
        return Response.ok(generator.getNextBatch()
                .stream()
                .collect(Json::createArrayBuilder, JsonArrayBuilder::add, JsonArrayBuilder::add)
                .build()
        ).build();
    }
}
