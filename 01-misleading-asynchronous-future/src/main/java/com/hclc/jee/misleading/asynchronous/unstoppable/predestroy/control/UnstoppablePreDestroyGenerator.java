package com.hclc.jee.misleading.asynchronous.unstoppable.predestroy.control;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import javax.ejb.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.Logger;

import static java.lang.Thread.currentThread;

@Singleton
@ConcurrencyManagement(ConcurrencyManagementType.BEAN)
@TransactionManagement(TransactionManagementType.BEAN)
// Uncomment the line below to see that you are not able to undeploy the app once this singleton is running.
// @Startup
public class UnstoppablePreDestroyGenerator {
    private static final Logger LOG = Logger.getLogger(UnstoppablePreDestroyGenerator.class.getName());
    private static final int BATCH_SIZE = 1000;
    private final BlockingQueue<String> ids = new LinkedBlockingQueue<>(1000 * BATCH_SIZE);
    private Future<Void> future;

    @Resource
    SessionContext sessionContext;

    @PostConstruct
    public void startGeneration() {
        future = sessionContext.getBusinessObject(UnstoppablePreDestroyGenerator.class).generate();
    }

    @Asynchronous
    public Future<Void> generate() {
        try {
            while (!currentThread().isInterrupted()) {
                ids.put(UUID.randomUUID().toString());
            }
        } catch (InterruptedException ex) {
            // ignore
        }
        return new AsyncResult<>(null);
    }

    public Collection<String> getNextBatch() throws InterruptedException {
        Collection<String> idsBatch = new ArrayList<>(BATCH_SIZE);
        for (int i = 0; i < BATCH_SIZE; i++) {
            idsBatch.add(ids.take());
        }
        return idsBatch;
    }

    @PreDestroy
    public void failingStop() {
        LOG.info("!!! SURPRISE: you will never get here");
    }
}
