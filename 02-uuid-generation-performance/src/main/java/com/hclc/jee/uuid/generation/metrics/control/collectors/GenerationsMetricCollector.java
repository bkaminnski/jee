package com.hclc.jee.uuid.generation.metrics.control.collectors;

import com.hclc.jee.uuid.generation.metrics.entity.GenerationMetricMessage;

public interface GenerationsMetricCollector {
    void register();
    void collectGenerationTimeMetric(GenerationMetricMessage metricMessage);
}
