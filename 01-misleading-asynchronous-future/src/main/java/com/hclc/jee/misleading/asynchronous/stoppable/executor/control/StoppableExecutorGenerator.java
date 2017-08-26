package com.hclc.jee.misleading.asynchronous.stoppable.executor.control;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import javax.ejb.ConcurrencyManagement;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.enterprise.concurrent.ManagedExecutorService;
import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;

import static java.lang.Thread.currentThread;
import static javax.ejb.ConcurrencyManagementType.BEAN;

@Singleton
@ConcurrencyManagement(BEAN)
@Startup
public class StoppableExecutorGenerator {
    private final static int BATCH_SIZE = 1000;
    private final BlockingQueue<String> ids = new LinkedBlockingQueue<>(1000 * BATCH_SIZE);
    private Future<?> generationFuture;

    @Resource
    ManagedExecutorService managedExecutorService;

    @PostConstruct
    public void startGeneration() {
        generationFuture = managedExecutorService.submit(() -> generate());
    }

    private void generate() {
        try {
            while (!currentThread().isInterrupted()) {
                ids.put(UUID.randomUUID().toString());
            }
        } catch (InterruptedException ex) {
            // ignore
        }
    }

    public Collection<String> getNextBatch() throws InterruptedException {
        Collection<String> idsBatch = new ArrayList<>(BATCH_SIZE);
        for (int i = 0; i < BATCH_SIZE; i++) {
            idsBatch.add(ids.take());
        }
        return idsBatch;
    }

    @PreDestroy
    public void stopGeneration() {
        generationFuture.cancel(true);
    }
}
