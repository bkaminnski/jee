package com.hclc.jee.uuid.generation.batch.singlequeue.control;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import javax.ejb.ConcurrencyManagement;
import javax.ejb.ConcurrencyManagementType;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.enterprise.concurrent.ManagedExecutorService;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.stream.Stream;

import static com.hclc.jee.uuid.generation.Parameters.BATCH_SINGLE_QUEUE_GENERATION_THREADS;
import static com.hclc.jee.uuid.generation.Parameters.BATCH_SINGLE_QUEUE_NUMBER_OF_CACHED_BATCHES;

@Singleton
@ConcurrencyManagement(ConcurrencyManagementType.BEAN)
@Startup
public class BatchSingleQueueGenerator {
    private final BlockingQueue<String[]> batches = new LinkedBlockingQueue<>(BATCH_SINGLE_QUEUE_NUMBER_OF_CACHED_BATCHES);
    private List<Future<?>> futures = new ArrayList<>(BATCH_SINGLE_QUEUE_GENERATION_THREADS);

    @Resource(lookup = "java:jboss/ee/concurrency/executor/batchSingleQueueGeneratorExecutor")
    ManagedExecutorService batchSingleQueueGeneratorExecutor;

    @PostConstruct
    public void startGeneration() {
        for (int i = 0; i < BATCH_SINGLE_QUEUE_GENERATION_THREADS; i++) {
            futures.add(batchSingleQueueGeneratorExecutor.submit(new BatchSingleQueuesGenerationThread(batches)));
        }
    }

    public JsonArray getNextBatch() {
        try {
            return Stream.of(batches.take())
                    .collect(Json::createArrayBuilder, JsonArrayBuilder::add, JsonArrayBuilder::add)
                    .build();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public JsonObject status() {
        return Json.createObjectBuilder()
                .add("size", batches.size())
                .add("max", BATCH_SINGLE_QUEUE_NUMBER_OF_CACHED_BATCHES)
                .build();
    }

    @PreDestroy
    public void stopGeneration() {
        futures.stream().forEach(f -> f.cancel(true));
    }
}
