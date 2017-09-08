package com.hclc.jee.uuid.generation.metrics.control;

import com.hclc.jee.uuid.generation.metrics.entity.GenerationTimeMetricMessage;

import javax.enterprise.event.Event;
import javax.inject.Inject;
import javax.interceptor.AroundInvoke;
import javax.interceptor.InvocationContext;

public class GenerationTimeMetricInterceptor {

    private static final double NANO_TO_SECONDS = 0.000_000_001;

    @Inject
    Event<GenerationTimeMetricMessage> generationTimeMetricsChannel;

    @AroundInvoke
    public Object gatherTimeMetric(InvocationContext invocationContext) throws Exception {
        long start = System.nanoTime();
        try {
            return invocationContext.proceed();
        } finally {
            generationTimeMetricsChannel.fire(
                    new GenerationTimeMetricMessage(
                            invocationContext.getMethod().getDeclaringClass().getSimpleName(),
                            (System.nanoTime() - start) * NANO_TO_SECONDS
                    )
            );
        }
    }
}
