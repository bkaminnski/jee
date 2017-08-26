package com.hclc.jee.misleading.asynchronous.unstoppable.cancel.control;

import javax.ejb.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;

import static java.lang.Thread.currentThread;

@Singleton
@ConcurrencyManagement(ConcurrencyManagementType.BEAN)
@TransactionManagement(TransactionManagementType.BEAN)
public class UnstoppableCancelGenerator {
    private static final int BATCH_SIZE = 1000;
    private final BlockingQueue<String> ids = new LinkedBlockingQueue<>(1000 * BATCH_SIZE);

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
}
