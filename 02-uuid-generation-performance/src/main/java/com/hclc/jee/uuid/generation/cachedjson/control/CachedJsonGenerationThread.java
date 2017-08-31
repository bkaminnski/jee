package com.hclc.jee.uuid.generation.cachedjson.control;

import com.hclc.jee.uuid.generation.UuidsGenerator;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import java.util.concurrent.BlockingQueue;
import java.util.stream.Stream;

class CachedJsonGenerationThread implements Runnable {
    private final BlockingQueue<JsonArray> batches;
    private volatile boolean continueGeneration = true;

    CachedJsonGenerationThread(BlockingQueue<JsonArray> batches) {
        this.batches = batches;
    }

    @Override
    public void run() {
        try {
            while (continueGeneration) {
                JsonArray batch = Stream.of(UuidsGenerator.generateBatch())
                        .collect(Json::createArrayBuilder, JsonArrayBuilder::add, JsonArrayBuilder::add)
                        .build();
                batches.put(batch);
            }
        } catch (InterruptedException ex) {
            // ignore
        }
    }

    void stopGeneration() {
        continueGeneration = false;
    }
}
