package com.hclc.jee.uuid.generation.batch.cachedjson.control;

import com.hclc.jee.uuid.generation.UuidsGenerator;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import java.util.concurrent.BlockingQueue;
import java.util.stream.Stream;

import static java.lang.Thread.currentThread;

class CachedJsonGenerationThread implements Runnable {
    private final BlockingQueue<JsonArray> batches;

    CachedJsonGenerationThread(BlockingQueue<JsonArray> batches) {
        this.batches = batches;
    }

    @Override
    public void run() {
        try {
            while (!currentThread().isInterrupted()) {
                JsonArray batch = Stream.of(UuidsGenerator.generateBatch())
                        .collect(Json::createArrayBuilder, JsonArrayBuilder::add, JsonArrayBuilder::add)
                        .build();
                batches.put(batch);
            }
        } catch (InterruptedException ex) {
            // ignore
        }
    }
}
