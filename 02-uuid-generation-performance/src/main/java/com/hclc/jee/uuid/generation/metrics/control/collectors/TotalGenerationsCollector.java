package com.hclc.jee.uuid.generation.metrics.control.collectors;

import com.hclc.jee.uuid.generation.metrics.entity.GenerationMetricMessage;
import io.prometheus.client.Counter;

import javax.ejb.Singleton;

@Singleton
public class TotalGenerationsCollector implements GenerationsMetricCollector {
    private Counter totalGenerationsCounter;

    @Override
    public void register() {
        totalGenerationsCounter = Counter.build()
                .name("generations_total")
                .help("Total number of generations")
                .labelNames("generator_name")
                .register();
    }

    @Override
    public void collectGenerationTimeMetric(GenerationMetricMessage metricMessage) {
        totalGenerationsCounter.labels(metricMessage.getGeneratorName()).inc();
    }
}
