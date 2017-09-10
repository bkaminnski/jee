package com.hclc.jee.uuid.generation.metrics.control.collectors;

import com.hclc.jee.uuid.generation.metrics.entity.GenerationMetricMessage;
import io.prometheus.client.Counter;

import javax.ejb.Singleton;

@Singleton
public class GenerationsDurationCollector implements GenerationsMetricCollector {
    private Counter generationsDurationCounter;

    @Override
    public void register() {
        generationsDurationCounter = Counter.build()
                .name("generations_duration")
                .help("Total duration of generations")
                .labelNames("generator_name")
                .register();
    }

    @Override
    public void collectGenerationTimeMetric(GenerationMetricMessage metricMessage) {
        generationsDurationCounter.labels(metricMessage.getGeneratorName()).inc(metricMessage.getDurationInSeconds());
    }
}
