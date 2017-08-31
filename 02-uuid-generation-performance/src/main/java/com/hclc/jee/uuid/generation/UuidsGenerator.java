package com.hclc.jee.uuid.generation;

import java.util.UUID;

import static com.hclc.jee.uuid.generation.Parameters.BATCH_SIZE;

public class UuidsGenerator {

    public static String[] generateBatch() {
        String[] idsBatch = new String[BATCH_SIZE];
        for (int i = 0; i < BATCH_SIZE; i++) {
            idsBatch[i] = UUID.randomUUID().toString();
        }
        return idsBatch;
    }
}
