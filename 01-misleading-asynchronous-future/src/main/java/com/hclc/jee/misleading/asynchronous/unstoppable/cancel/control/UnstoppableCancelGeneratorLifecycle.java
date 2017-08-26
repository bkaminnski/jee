package com.hclc.jee.misleading.asynchronous.unstoppable.cancel.control;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;
import java.util.concurrent.Future;
import java.util.logging.Logger;

@Singleton
// Uncomment the line below to see that you are not able to undeploy the app once this singleton is running.
// @Startup
public class UnstoppableCancelGeneratorLifecycle {
    private static final Logger LOG = Logger.getLogger(UnstoppableCancelGeneratorLifecycle.class.getName());
    private Future<Void> future;

    @Inject
    UnstoppableCancelGenerator generator;

    @PostConstruct
    public void startGeneration() {
        future = generator.generate();
    }

    @PreDestroy
    public void failingStop() {
        LOG.info("!!! stopping generation...");
        boolean cancellationResult = future.cancel(true);
        LOG.info("!!! SURPRISE: future was not cancelled; cancellationResult=" + cancellationResult);
    }
}