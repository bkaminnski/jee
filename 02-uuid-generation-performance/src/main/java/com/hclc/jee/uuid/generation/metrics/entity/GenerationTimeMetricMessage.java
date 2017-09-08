package com.hclc.jee.uuid.generation.metrics.entity;

public class GenerationTimeMetricMessage {
    private final String generatorName;
    private final double timeInNanos;

    public GenerationTimeMetricMessage(String generatorName, double timeInNanos) {
        this.generatorName = generatorName;
        this.timeInNanos = timeInNanos;
    }

    public String getGeneratorName() {
        return generatorName;
    }

    public double getTimeInSeconds() {
        return timeInNanos;
    }
}
