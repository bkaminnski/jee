package com.hclc.jee.uuid.generation.batch.singlequeue.boundary;

import com.hclc.jee.uuid.generation.batch.singlequeue.control.BatchSingleQueueGenerator;
import com.hclc.jee.uuid.generation.metrics.control.GenerationMetricInterceptor;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.interceptor.Interceptors;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

@Stateless
@Path(value = "batchSingleQueue")
public class BatchSingleQueueResource {

    @Inject
    BatchSingleQueueGenerator batchSingleQueueGenerator;

    @GET
    @Produces(APPLICATION_JSON)
    @Interceptors(GenerationMetricInterceptor.class)
    public Response batchSingleQueue() {
        return Response.ok(batchSingleQueueGenerator.getNextBatch()).build();
    }

    @GET
    @Produces(APPLICATION_JSON)
    @Path("status")
    public Response status() {
        return Response.ok(batchSingleQueueGenerator.status()).build();
    }
}
