To add in the main readme:

1. [Performance of UUID generation](https://github.com/bkaminnski/jee/tree/master/02-uuid-generation-performance) - couple possible approaches, performance analysis and comparison (JMeter), white-box monitoring (Prometheus, Grafana), conclusions.


# Performance of UUID generation

This project compares straightforward vs batchSingleQueue performance of UUID generation.




"c:\Program Files\Java\jdk1.8.0_121\bin\jvisualvm.exe" -cp:a c:\Projekty\sofcik\wildfly-10.1.0.Final\bin\client\jboss-cli-client.jar 

service:jmx:http-remoting-jmx://localhost:9990

Check `Use security credentials` and put `admin` as username and `admin` as password.