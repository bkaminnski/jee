package com.hclc.jee.uuid.generation.metrics.control.collectors;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.LinkedList;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

import static java.time.Instant.now;
import static java.time.temporal.ChronoUnit.SECONDS;

class DelayedObservations {
    private final long timeToKeepObservation;
    private final ChronoUnit unit;
    private final DelayQueue<DelayedObservation> observations = new DelayQueue<>();

    DelayedObservations(long timeToKeepObservation, ChronoUnit unit) {
        this.timeToKeepObservation = timeToKeepObservation;
        this.unit = unit;
    }

    void observe(double value) {
        observations.drainTo(new LinkedList<>());
        observations.add(new DelayedObservation(value));
    }

    double max() {
        return observations.stream().mapToDouble(DelayedObservation::getObservation).max().orElse(0.0);
    }

    private class DelayedObservation implements Delayed {
        private final double observation;
        private final Instant validUntil;

        DelayedObservation(double observation) {
            this.observation = observation;
            this.validUntil = now().plus(timeToKeepObservation, unit);
        }

        public double getObservation() {
            return observation;
        }

        @Override
        public long getDelay(TimeUnit unit) {
            return unit.convert(SECONDS.between(now(), validUntil), TimeUnit.SECONDS);
        }

        @Override
        public int compareTo(Delayed other) {
            long currentDelay = this.getDelay(TimeUnit.SECONDS);
            long otherDelay = other.getDelay(TimeUnit.SECONDS);
            if (currentDelay > otherDelay)
                return 1;
            if (currentDelay < otherDelay)
                return -1;
            return 0;
        }
    }
}
