package com.hclc.jee.misleading.asynchronous.stoppable.asynchronous.nonblocking.control;

import javax.annotation.Resource;
import javax.ejb.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;

import static java.util.concurrent.TimeUnit.MILLISECONDS;

@Singleton
@ConcurrencyManagement(ConcurrencyManagementType.BEAN)
@TransactionManagement(TransactionManagementType.BEAN)
public class NonblockingGenerator {
    private static final int BATCH_SIZE = 1000;
    private final BlockingQueue<String> ids = new LinkedBlockingQueue<>(1000 * BATCH_SIZE);

    @Resource
    SessionContext sessionContext;

    @Asynchronous
    public Future<Void> generate() {
        try {
            while (!sessionContext.wasCancelCalled()) {
                ids.offer(UUID.randomUUID().toString(), 500, MILLISECONDS);
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
