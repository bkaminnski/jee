package com.hclc.jee.uuid.generation.random.boundary;


import com.hclc.jee.uuid.generation.batch.cachedjson.boundary.CachedJsonResource;
import com.hclc.jee.uuid.generation.batch.multiplequeues.boundary.BatchMultipleQueuesResource;
import com.hclc.jee.uuid.generation.batch.singlequeue.boundary.BatchSingleQueueResource;
import com.hclc.jee.uuid.generation.onebyone.boundary.OneByOneResource;
import com.hclc.jee.uuid.generation.random.entity.GeneratorCallback;
import com.hclc.jee.uuid.generation.reference.boundary.ReferenceResource;
import com.hclc.jee.uuid.generation.straightforward.boundary.StraightforwardResource;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

@Stateless
@Path(value = "random")
public class RandomResource {

    @Inject
    ReferenceResource referenceResource;

    @Inject
    StraightforwardResource straightforwardResource;

    @Inject
    OneByOneResource oneByOneResource;

    @Inject
    BatchSingleQueueResource batchSingleQueueResource;

    @Inject
    BatchMultipleQueuesResource batchMultipleQueuesResource;

    @Inject
    CachedJsonResource cachedJsonResource;

    @GET
    @Produces(APPLICATION_JSON)
    public Response random() {
        GeneratorCallback[] callbacks = {
                () -> referenceResource.reference(),
                () -> straightforwardResource.straightforward(),
                () -> oneByOneResource.oneByOne(),
                () -> batchSingleQueueResource.batchSingleQueue(),
                () -> batchMultipleQueuesResource.batchMultipleQueues(),
                () -> cachedJsonResource.cachedJson()
        };
        return callbacks[(int) (Math.random() * callbacks.length)].get();
    }
}
