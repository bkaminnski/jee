package com.hclc.jee.uuid.generation.metrics.boundary;

import com.hclc.jee.uuid.generation.metrics.entity.GenerationTimeMetricMessage;
import io.prometheus.client.CollectorRegistry;
import io.prometheus.client.Counter;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.enterprise.event.Observes;

@Singleton
@Startup
public class MetricsCollector {
    private Counter generationCounter;

    @PostConstruct
    public void registerCollectors() {
        generationCounter = Counter.build()
                .name("generations_total")
                .help("Total number of generations")
                .labelNames("generator_name")
                .register();
    }

    public void collectGenerationTimeMetric(@Observes GenerationTimeMetricMessage metricMessage) {
        String generatorName = metricMessage.getGeneratorName();
        double timeInSeconds = metricMessage.getTimeInSeconds();

        generationCounter.labels(generatorName).inc();
    }

    @PreDestroy
    public void unregisterCollectors() {
        CollectorRegistry.defaultRegistry.clear();
    }
}
