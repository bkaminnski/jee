package com.hclc.jee.uuid.generation.reference.boundary;

import com.hclc.jee.uuid.generation.metrics.control.GenerationMetricInterceptor;

import javax.ejb.Stateless;
import javax.interceptor.Interceptors;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import static com.hclc.jee.uuid.generation.UuidsGenerator.generateBatch;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Stream.of;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

@Stateless
@Path(value = "reference")
public class ReferenceResource {
    private static final String referenceResponse;

    static {
        String commaSeparatedUuids = of(generateBatch())
                .map(u -> "\"" + u + "\"")
                .collect(joining(","));
        referenceResponse = "[" + commaSeparatedUuids + "]";
    }

    @GET
    @Produces(APPLICATION_JSON)
    @Interceptors(GenerationMetricInterceptor.class)
    public Response reference() {
        return Response.ok(referenceResponse).build();
    }
}
