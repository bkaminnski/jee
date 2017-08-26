package com.hclc.jee.misleading.asynchronous.stoppable.asynchronous.tamperingwithlogic.control;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

class GeneratorGuts {
    static final int BATCH_SIZE = 1000;
    private final BlockingQueue<String> ids = new LinkedBlockingQueue<>(1000 * BATCH_SIZE);
    private volatile boolean continueGeneration = true;

    BlockingQueue<String> getIds() {
        return ids;
    }

    boolean isContinueGeneration() {
        return continueGeneration;
    }

    void stopGeneration() {
        continueGeneration = false;
        ids.poll();
    }
}
