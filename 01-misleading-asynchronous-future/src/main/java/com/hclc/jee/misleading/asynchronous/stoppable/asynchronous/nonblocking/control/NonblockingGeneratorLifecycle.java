package com.hclc.jee.misleading.asynchronous.stoppable.asynchronous.nonblocking.control;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;
import java.util.concurrent.Future;

@Singleton
@Startup
public class NonblockingGeneratorLifecycle {
    private Future<Void> future;

    @Inject
    NonblockingGenerator generator;

    @PostConstruct
    public void startGeneration() {
        future = generator.generate();
    }

    @PreDestroy
    public void stopGeneration() {
        future.cancel(true);
    }
}