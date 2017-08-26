package com.hclc.jee.misleading.asynchronous.unstoppable.cancel.control;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;
import java.util.concurrent.Future;
import java.util.logging.Logger;

@Singleton
@Startup
public class UnstoppableCancelGeneratorLifecycle {
    private final static Logger LOG = Logger.getLogger(UnstoppableCancelGeneratorLifecycle.class.getName());
    private Future<Void> generationFuture;

    @Inject
    UnstoppableCancelGenerator generator;

    @PostConstruct
    public void startGeneration() {
        generationFuture = generator.generate();
    }

    @PreDestroy
    public void failingStop() {
        LOG.info("!!! stopping generation...");
        boolean cancellationResult = generationFuture.cancel(true);
        LOG.info("!!! SURPRISE: generationFuture was not cancelled; cancellationResult=" + cancellationResult);
    }
}