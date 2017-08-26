package com.hclc.jee.misleading.asynchronous.stoppable.asynchronous.tamperingwithlogic.control;

import javax.ejb.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;

import static com.hclc.jee.misleading.asynchronous.stoppable.asynchronous.tamperingwithlogic.control.GeneratorGuts.BATCH_SIZE;

@Singleton
@ConcurrencyManagement(ConcurrencyManagementType.BEAN)
@TransactionManagement(TransactionManagementType.BEAN)
public class TamperingWithLogicGenerator {
    private GeneratorGuts generatorGuts;

    @Asynchronous
    public void generate(GeneratorGuts generatorGuts) {
        this.generatorGuts = generatorGuts;
        try {
            while (generatorGuts.isContinueGeneration()) {
                generatorGuts.getIds().put(UUID.randomUUID().toString());
            }
        } catch (InterruptedException ex) {
            // ignore
        }
    }

    public Collection<String> getNextBatch() throws InterruptedException {
        Collection<String> idsBatch = new ArrayList<>(BATCH_SIZE);
        for (int i = 0; i < BATCH_SIZE; i++) {
            idsBatch.add(generatorGuts.getIds().take());
        }
        return idsBatch;
    }
}
