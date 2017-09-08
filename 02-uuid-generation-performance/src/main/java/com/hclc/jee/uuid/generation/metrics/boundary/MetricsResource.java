package com.hclc.jee.uuid.generation.metrics.boundary;

import io.prometheus.client.CollectorRegistry;
import io.prometheus.client.exporter.common.TextFormat;

import javax.ejb.Stateless;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.StreamingOutput;
import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import java.io.Writer;

@Stateless
@Path("metrics")
public class MetricsResource {

    @GET
    @Produces("text/plain")
    public Response get() {
        StreamingOutput streamingOutput = outputStream -> {
            Writer bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream));
            TextFormat.write004(bufferedWriter, CollectorRegistry.defaultRegistry.metricFamilySamples());
            bufferedWriter.flush();
        };

        return Response.ok(streamingOutput).build();
    }
}
