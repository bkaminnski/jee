package com.hclc.jee.uuid.generation.onebyone.boundary;


import com.hclc.jee.uuid.generation.metrics.control.GenerationTimeMetricInterceptor;
import com.hclc.jee.uuid.generation.onebyone.control.OneByOneGenerator;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.interceptor.Interceptors;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

@Stateless
@Path(value = "oneByOne")
@Interceptors(GenerationTimeMetricInterceptor.class)
public class OneByOneResource {

    @Inject
    OneByOneGenerator oneByOneGenerator;

    @GET
    @Produces(APPLICATION_JSON)
    public Response oneByOne() {
        return Response.ok(oneByOneGenerator.getNextBatch()).build();
    }

    @GET
    @Produces(APPLICATION_JSON)
    @Path("status")
    public Response status() {
        return Response.ok(oneByOneGenerator.status()).build();
    }
}
