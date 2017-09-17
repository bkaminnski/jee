package com.hclc.jee.uuid.generation.batch.multiplequeues.control;

import com.hclc.jee.uuid.generation.UuidsGenerator;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import static com.hclc.jee.uuid.generation.Parameters.BATCH_MULTIPLE_QUEUES_NUMBER_OF_CACHED_BATCHES;
import static java.lang.Thread.currentThread;

class BatchMultipleQueuesGenerationThread implements Runnable {
    private final BlockingQueue<String[]> batches = new LinkedBlockingQueue<>(BATCH_MULTIPLE_QUEUES_NUMBER_OF_CACHED_BATCHES);

    @Override
    public void run() {
        try {
            while (!currentThread().isInterrupted()) {
                batches.put(UuidsGenerator.generateBatch());
            }
        } catch (InterruptedException ex) {
            // ignore
        }
    }

    String[] getNextBatch() {
        try {
            return batches.take();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    int getCurrentSize() {
        return batches.size();
    }
}
