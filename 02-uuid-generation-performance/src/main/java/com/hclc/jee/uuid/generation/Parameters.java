package com.hclc.jee.uuid.generation;

public class Parameters {
    public static final int BATCH_SIZE = 1000;

    public static final int ONE_BY_ONE_NUMBER_OF_CACHED_IDS = 1000 * BATCH_SIZE;
    public static final int ONE_BY_ONE_GENERATION_THREADS = 4;

    public static final int BATCH_SINGLE_QUEUE_NUMBER_OF_CACHED_BATCHES = 1000;
    public static final int BATCH_SINGLE_QUEUE_GENERATION_THREADS = 4;

    public static final int BATCH_MULTIPLE_QUEUES_NUMBER_OF_CACHED_BATCHES = 250;
    public static final int BATCH_MULTIPLE_QUEUES_GENERATION_THREADS = 4;

    public static final int CACHED_JSON_NUMBER_OF_CACHED_BATCHES = 1000;
    public static final int CACHED_JSON_GENERATION_THREADS = 4;
}
