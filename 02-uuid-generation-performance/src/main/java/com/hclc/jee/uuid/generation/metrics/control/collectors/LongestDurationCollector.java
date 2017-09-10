package com.hclc.jee.uuid.generation.metrics.control.collectors;

import com.hclc.jee.uuid.generation.metrics.entity.GenerationMetricMessage;
import io.prometheus.client.Gauge;

import javax.ejb.Singleton;
import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.Queue;

@Singleton
public class LongestDurationCollector implements GenerationsMetricCollector {
    private static final int NUMBER_OF_OBSERVATIONS = 1000;
    private Gauge longestDurationGauge;
    private HashMap<String, Queue<Double>> lastNObservationsPerGeneratorName = new HashMap<>();

    @Override
    public void register() {
        longestDurationGauge = Gauge.build()
                .name("longest_duration_within_last_n_generations")
                .help("Longest duration within last n generations")
                .labelNames("generator_name", "n")
                .register();
    }

    @Override
    public void collectGenerationTimeMetric(GenerationMetricMessage metricMessage) {
        Queue<Double> lastNObservations = lastNObservationsPerGeneratorName.computeIfAbsent(metricMessage.getGeneratorName(), k -> new ArrayDeque<>(NUMBER_OF_OBSERVATIONS + 1));
        lastNObservations.add(metricMessage.getDurationInSeconds());
        if (lastNObservations.size() > NUMBER_OF_OBSERVATIONS) {
            lastNObservations.poll();
        }
        longestDurationGauge.labels(metricMessage.getGeneratorName(), String.valueOf(NUMBER_OF_OBSERVATIONS)).set(lastNObservations.stream().max(Double::compare).get());
    }
}
