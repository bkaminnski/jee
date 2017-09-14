FROM prom/prometheus
MAINTAINER Bartosz Kamiński, highcohesionloosecoupling.com
ADD prometheus.yml /etc/prometheus/
ADD alerts.rules /etc/prometheus/
CMD [ "-config.file=/etc/prometheus/prometheus.yml", \
      "-storage.local.path=/prometheus", \
      "-web.console.libraries=/usr/share/prometheus/console_libraries", \
      "-web.console.templates=/usr/share/prometheus/consoles", \
      "-alertmanager.url=http://alertmanager-configured:9093" ]