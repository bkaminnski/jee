package com.hclc.jee.misleading.asynchronous.stoppable.asynchronous.tamperingwithlogic.control;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;

@Singleton
@Startup
public class TamperingWithLogicGeneratorLifecycle {
    private final GeneratorGuts generatorGuts = new GeneratorGuts();

    @Inject
    TamperingWithLogicGenerator generator;

    @PostConstruct
    public void startGeneration() {
        generator.generate(generatorGuts);
    }

    @PreDestroy
    public void stopGeneration() {
        generatorGuts.stopGeneration();
    }
}