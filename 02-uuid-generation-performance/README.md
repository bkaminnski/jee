## Performance of UUID generation

Work in progress:

- DONE
    - couple possible approaches, 
    - performance analysis and comparison (JMeter)
    - white-box metrics (Prometheus, Grafana)
    - docker environment containg wildfly, prometheus, grafana automatically configured on startup
    - a single counter metric measuring a fact that generation occurred
    - a single dashboard with two diagrams 
        - one showing a rate of generations per second
        - one showing total number of generations
- PLANNED
    - more work on metrics (histogram based on gauge measuring duration of each generation)
    - correlation with CPU usage (based on JVM metrics)
    - queues status metric
    - alert configuration