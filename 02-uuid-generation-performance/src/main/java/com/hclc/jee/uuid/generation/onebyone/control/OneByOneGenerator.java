package com.hclc.jee.uuid.generation.onebyone.control;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import javax.ejb.ConcurrencyManagement;
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

import static com.hclc.jee.uuid.generation.Parameters.*;
import static javax.ejb.ConcurrencyManagementType.BEAN;

@Singleton
@ConcurrencyManagement(BEAN)
@Startup
public class OneByOneGenerator {
    private final BlockingQueue<String> ids = new LinkedBlockingQueue<>(ONE_BY_ONE_NUMBER_OF_CACHED_IDS);
    private List<Future<?>> futures = new ArrayList<>(ONE_BY_ONE_GENERATION_THREADS);

    @Resource(lookup = "java:jboss/ee/concurrency/executor/oneByOneGeneratorExecutor")
    ManagedExecutorService oneByOneGeneratorExecutor;

    @PostConstruct
    public void startGeneration() {
        for (int i = 0; i < ONE_BY_ONE_GENERATION_THREADS; i++) {
            futures.add(oneByOneGeneratorExecutor.submit(new OneByOneGenerationThread(ids)));
        }
    }

    public JsonArray getNextBatch() {
        try {
            String[] idsBatch = new String[BATCH_SIZE];
            for (int i = 0; i < BATCH_SIZE; i++) {
                idsBatch[i] = ids.take();
            }
            return Stream.of(idsBatch)
                    .collect(Json::createArrayBuilder, JsonArrayBuilder::add, JsonArrayBuilder::add)
                    .build();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public JsonObject status() {
        return Json.createObjectBuilder()
                .add("size", ids.size())
                .add("max", ONE_BY_ONE_NUMBER_OF_CACHED_IDS)
                .build();
    }

    public double fillFactor() {
        return 1.0 * ids.size() / ONE_BY_ONE_NUMBER_OF_CACHED_IDS;
    }

    @PreDestroy
    public void stopGeneration() {
        futures.stream().forEach(f -> f.cancel(true));
    }
}
