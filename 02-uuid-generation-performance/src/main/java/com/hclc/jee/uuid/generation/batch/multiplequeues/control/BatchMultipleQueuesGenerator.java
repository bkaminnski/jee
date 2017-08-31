package com.hclc.jee.uuid.generation.batch.multiplequeues.control;

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
import java.util.concurrent.Future;
import java.util.stream.Stream;

import static com.hclc.jee.uuid.generation.Parameters.BATCH_MULTIPLE_QUEUES_GENERATION_THREADS;
import static com.hclc.jee.uuid.generation.Parameters.BATCH_MULTIPLE_QUEUES_NUMBER_OF_CACHED_BATCHES;
import static javax.json.Json.createObjectBuilder;

@Singleton
@ConcurrencyManagement(ConcurrencyManagementType.BEAN)
@Startup
public class BatchMultipleQueuesGenerator {
    private List<BatchMultipleQueuesGenerationThread> threads = new ArrayList<>(BATCH_MULTIPLE_QUEUES_GENERATION_THREADS);
    private List<Future<?>> futures = new ArrayList<>(BATCH_MULTIPLE_QUEUES_GENERATION_THREADS);

    @Resource(lookup = "java:jboss/ee/concurrency/executor/batchMultipleQueuesGeneratorExecutor")
    ManagedExecutorService batchMultipleQueuesGeneratorExecutor;

    @PostConstruct
    public void startGeneration() {
        for (int i = 0; i < BATCH_MULTIPLE_QUEUES_GENERATION_THREADS; i++) {
            BatchMultipleQueuesGenerationThread thread = new BatchMultipleQueuesGenerationThread();
            threads.add(thread);
            futures.add(batchMultipleQueuesGeneratorExecutor.submit(thread));
        }
    }

    public JsonArray getNextBatch() {
        return Stream.of(aBatchFromRandomThread())
                .collect(Json::createArrayBuilder, JsonArrayBuilder::add, JsonArrayBuilder::add)
                .build();
    }

    private String[] aBatchFromRandomThread() {
        return threads
                .get((int) (Math.random() * BATCH_MULTIPLE_QUEUES_GENERATION_THREADS))
                .getNextBatch();
    }

    public JsonObject status() {
        return createObjectBuilder()
                .add("summary", createObjectBuilder()
                        .add("size", threads.stream().mapToInt(BatchMultipleQueuesGenerationThread::getCurrentSize).sum())
                        .add("max", BATCH_MULTIPLE_QUEUES_GENERATION_THREADS * BATCH_MULTIPLE_QUEUES_NUMBER_OF_CACHED_BATCHES))
                .add("threads", threads.stream()
                        .map(t -> createObjectBuilder()
                                .add("size", t.getCurrentSize())
                                .add("max", BATCH_MULTIPLE_QUEUES_NUMBER_OF_CACHED_BATCHES))
                        .collect(Json::createArrayBuilder, JsonArrayBuilder::add, JsonArrayBuilder::add))
                .build();
    }

    @PreDestroy
    public void stopGeneration() {
        threads.stream().forEach(BatchMultipleQueuesGenerationThread::stopGeneration);
        futures.stream().forEach(f -> f.cancel(true));
    }
}
