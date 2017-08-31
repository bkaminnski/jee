package com.hclc.jee.uuid.generation.batch.singlequeue.control;

import com.hclc.jee.uuid.generation.UuidsGenerator;

import java.util.concurrent.BlockingQueue;

import static java.lang.Thread.currentThread;

class BatchSingleQueuesGenerationThread implements Runnable {
    private final BlockingQueue<String[]> batches;

    BatchSingleQueuesGenerationThread(BlockingQueue<String[]> batches) {
        this.batches = batches;
    }

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
}
