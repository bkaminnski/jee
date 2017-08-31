package com.hclc.jee.uuid.generation.onebyone.control;

import java.util.UUID;
import java.util.concurrent.BlockingQueue;

import static java.lang.Thread.currentThread;

class OneByOneGenerationThread implements Runnable {
    private final BlockingQueue<String> ids;

    OneByOneGenerationThread(BlockingQueue<String> ids) {
        this.ids = ids;
    }

    @Override
    public void run() {
        try {
            while (!currentThread().isInterrupted()) {
                ids.put(UUID.randomUUID().toString());
            }
        } catch (InterruptedException ex) {
            // ignore
        }
    }
}
