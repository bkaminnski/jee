package com.hclc.jee.uuid.generation.metrics.control.collectors;

import org.junit.Before;
import org.junit.Test;

import static java.time.temporal.ChronoUnit.MILLIS;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.closeTo;

public class DelayedObservationsTest {
    private DelayedObservations observations;

    @Before
    public void setUp() {
        observations = new DelayedObservations(50, MILLIS);
    }

    @Test
    public void shouldReturnCorrectMaxWithinDelay() {
        observations.observe(1);
        observations.observe(2);
        observations.observe(3);

        assertThat(observations.max(), closeTo(3.0, 0.01));
    }

    @Test
    public void shouldReturnCorrectMaxAfterDelayElapsedAndNewObservationWasObserved() {
        observations.observe(1);
        observations.observe(2);
        observations.observe(3);
        waitUntilDelayElapses();
        observations.observe(0.5);

        assertThat(observations.max(), closeTo(0.5, 0.01));
    }

    @Test
    public void shouldMaintainMaxAfterDelayElapsedWithNoNewObservation() {
        observations.observe(1);
        observations.observe(2);
        observations.observe(3);
        waitUntilDelayElapses();

        assertThat(observations.max(), closeTo(3.0, 0.01));
    }

    private void waitUntilDelayElapses() {
        try {
            Thread.sleep(70);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}