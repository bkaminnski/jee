package com.hclc.jee.uuid.generation.batch.multiplequeues.boundary;

import com.hclc.jee.uuid.generation.batch.multiplequeues.control.BatchMultipleQueuesGenerator;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

@Stateless
@Path(value = "batchMultipleQueues")
public class BatchMultipleQueuesResource {

    @Inject
    BatchMultipleQueuesGenerator batchMultipleQueuesGenerator;

    @GET
    @Produces(APPLICATION_JSON)
    public Response batchMultipleQueues() {
        return Response.ok(batchMultipleQueuesGenerator.getNextBatch()).build();
    }

    @GET
    @Produces(APPLICATION_JSON)
    @Path("status")
    public Response status() {
        return Response.ok(batchMultipleQueuesGenerator.status()).build();
    }
}
