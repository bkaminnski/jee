package com.hclc.jee.uuid.generation.metrics.control.collectors;

import com.hclc.jee.uuid.generation.metrics.entity.GenerationMetricMessage;
import io.prometheus.client.Gauge;

import javax.ejb.Singleton;
import java.util.HashMap;

import static java.time.temporal.ChronoUnit.SECONDS;

@Singleton
public class LongestDurationCollector implements GenerationsMetricCollector {
    private static final int TIME_TO_KEEP_OBSERVATION_IN_SECONDS = 5;
    private Gauge longestDurationGauge;
    private HashMap<String, DelayedObservations> observationsPerGeneratorName = new HashMap<>();

    @Override
    public void register() {
        longestDurationGauge = Gauge.build()
                .name("longest_duration_within_last_n_seconds")
                .help("Longest duration within last n seconds")
                .labelNames("generator_name", "n")
                .register();
    }

    @Override
    public void collectGenerationTimeMetric(GenerationMetricMessage metricMessage) {
        DelayedObservations observations = observationsPerGeneratorName.computeIfAbsent(metricMessage.getGeneratorName(), k -> new DelayedObservations(TIME_TO_KEEP_OBSERVATION_IN_SECONDS, SECONDS));
        observations.observe(metricMessage.getDurationInSeconds());
        longestDurationGauge
                .labels(metricMessage.getGeneratorName(), String.valueOf(TIME_TO_KEEP_OBSERVATION_IN_SECONDS))
                .set(observations.max());
    }
}
