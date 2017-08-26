package com.hclc.jee.misleading.asynchronous.stoppable.asynchronous.interruptingthread.control;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;

@Singleton
@Startup
public class InterruptingThreadGeneratorLifecycle {
    private final ThreadHolder threadHolder = new ThreadHolder();

    @Inject
    InterruptingThreadGenerator generator;

    @PostConstruct
    public void startGeneration() {
        generator.generate(threadHolder);
    }

    @PreDestroy
    public void stopGeneration() {
        threadHolder.interrupt();
    }
}