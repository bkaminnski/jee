package com.hclc.jee.uuid.generation.metrics.boundary;

import com.hclc.jee.uuid.generation.metrics.control.collectors.GenerationsMetricCollector;
import com.hclc.jee.uuid.generation.metrics.control.collectors.QueueFillFactorCollector;
import com.hclc.jee.uuid.generation.metrics.entity.GenerationMetricMessage;
import io.prometheus.client.CollectorRegistry;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.enterprise.event.Observes;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;

@Singleton
@Startup
public class MetricsCollector {

    @Inject
    Instance<GenerationsMetricCollector> generationsCollectors;

    @Inject
    QueueFillFactorCollector queueFillFactorCollector;

    @PostConstruct
    public void registerCollectors() {
        generationsCollectors.forEach(GenerationsMetricCollector::register);
        queueFillFactorCollector.register();
    }

    public void collectGenerationTimeMetric(@Observes GenerationMetricMessage metricMessage) {
        generationsCollectors.forEach(c -> c.collectGenerationTimeMetric(metricMessage));
    }

    @PreDestroy
    public void unregisterCollectors() {
        CollectorRegistry.defaultRegistry.clear();
    }
}
