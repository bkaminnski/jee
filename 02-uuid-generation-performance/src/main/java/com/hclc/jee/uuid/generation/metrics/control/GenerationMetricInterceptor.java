package com.hclc.jee.uuid.generation.metrics.control;

import com.hclc.jee.uuid.generation.metrics.entity.GenerationMetricMessage;

import javax.enterprise.event.Event;
import javax.inject.Inject;
import javax.interceptor.AroundInvoke;
import javax.interceptor.InvocationContext;

public class GenerationMetricInterceptor {

    private static final double NANO_TO_SECONDS = 0.000_000_001;

    @Inject
    Event<GenerationMetricMessage> generationMetricsChannel;

    @AroundInvoke
    public Object measure(InvocationContext invocationContext) throws Exception {
        long start = System.nanoTime();
        try {
            return invocationContext.proceed();
        } finally {
            generationMetricsChannel.fire(
                    new GenerationMetricMessage(
                            invocationContext.getMethod().getDeclaringClass().getSimpleName(),
                            (System.nanoTime() - start) * NANO_TO_SECONDS
                    )
            );
        }
    }
}
