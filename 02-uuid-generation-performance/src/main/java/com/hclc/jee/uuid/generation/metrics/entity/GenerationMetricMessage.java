package com.hclc.jee.uuid.generation.metrics.entity;

public class GenerationMetricMessage {
    private final String generatorName;
    private final double durationInSeconds;

    public GenerationMetricMessage(String generatorName, double durationInSeconds) {
        this.generatorName = generatorName;
        this.durationInSeconds = durationInSeconds;
    }

    public String getGeneratorName() {
        return generatorName;
    }

    public double getDurationInSeconds() {
        return durationInSeconds;
    }
}
