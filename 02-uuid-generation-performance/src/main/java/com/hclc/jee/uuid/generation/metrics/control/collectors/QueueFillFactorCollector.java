package com.hclc.jee.uuid.generation.metrics.control.collectors;

import com.hclc.jee.uuid.generation.batch.cachedjson.control.CachedJsonGenerator;
import com.hclc.jee.uuid.generation.batch.multiplequeues.control.BatchMultipleQueuesGenerator;
import com.hclc.jee.uuid.generation.batch.singlequeue.control.BatchSingleQueueGenerator;
import com.hclc.jee.uuid.generation.onebyone.boundary.OneByOneResource;
import com.hclc.jee.uuid.generation.onebyone.control.OneByOneGenerator;
import io.prometheus.client.Gauge;

import javax.ejb.Singleton;
import javax.inject.Inject;

@Singleton
public class QueueFillFactorCollector {
    private Gauge queueFillFactorGauge;

    @Inject
    OneByOneGenerator oneByOneGenerator;

    @Inject
    BatchMultipleQueuesGenerator batchMultipleQueuesGenerator;

    @Inject
    BatchSingleQueueGenerator batchSingleQueueGenerator;

    @Inject
    CachedJsonGenerator cachedJsonGenerator;

    public void register() {
        queueFillFactorGauge = Gauge.build()
                .name("queue_fill_factor")
                .help("Fill factor of queues")
                .labelNames("generator_name")
                .register();
        registerQueueFillFactorCallback(oneByOneGenerator::fillFactor, OneByOneResource.class.getSimpleName());
        registerQueueFillFactorCallback(batchSingleQueueGenerator::fillFactor, BatchSingleQueueGenerator.class.getSimpleName());
        registerQueueFillFactorCallback(batchMultipleQueuesGenerator::fillFactor, BatchMultipleQueuesGenerator.class.getSimpleName());
        registerQueueFillFactorCallback(cachedJsonGenerator::fillFactor, CachedJsonGenerator.class.getSimpleName());
    }

    private void registerQueueFillFactorCallback(MetricCallback metricCallback, String generatorName) {
        queueFillFactorGauge.setChild(new Gauge.Child() {
            @Override
            public double get() {
                return metricCallback.get();
            }
        }, generatorName);
    }

    interface MetricCallback {
        double get();
    }
}
