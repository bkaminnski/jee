#!/usr/bin/jjs -fv

load("./scripts/docker.js");
load('./scripts/command.js');

function middlewareUp() {
	var dockerNetworks = new DockerNetworks();
	dockerNetworks.assureExisists('jee');

	var dockerImages = new DockerImages();
	dockerImages.build('./docker/alertmanager', 'alertmanager-configured');
	dockerImages.build('./docker/prometheus', 'prometheus-configured');
	dockerImages.build('./docker/grafana', 'grafana-configured');
	dockerImages.build('./docker/java', 'java');
	dockerImages.build('./docker/wildfly', 'wildfly');
	dockerImages.build('./docker/wildfly-configured', 'wildfly-configured');

	var dockerContainers = new DockerContainers();
	var configureGrafanaFirstTime = !dockerContainers.exists('grafana-configured');

	dockerContainers.run('wildfly-configured', 'wildfly-configured', '-p 8080:8080 -p 9990:9990 -p 8787:8787 -p 7091:7091 --network jee');
	dockerContainers.waitFor('wildfly-configured', 'WildFly Full 10.1.0.Final (WildFly Core 2.2.0.Final) started');

	dockerContainers.run('alertmanager-configured', 'alertmanager-configured', '-p 9093:9093 --network jee');
	dockerContainers.waitFor('alertmanager-configured', 'Listening on :9093');

	dockerContainers.run('prometheus-configured', 'prometheus-configured', '-p 9090:9090 --network jee');
	dockerContainers.waitFor('prometheus-configured', 'Listening on :9090');

	dockerContainers.run('grafana-configured', 'grafana-configured', '-p 3000:3000 --network jee');
	dockerContainers.waitFor('grafana-configured', 'Initializing HTTP Server');
	if (configureGrafanaFirstTime) {
		configureGrafana();
	}
}

function configureGrafana() {
	addPrometheusDataSourceToGrafana();
	importDashboardToGrafana();
}

function addPrometheusDataSourceToGrafana() {
	var prometheusDatasourcesEndpoint = "http://admin:admin@localhost:3000/api/datasources";
	var configurationRequestHeaders = "Content-Type: application/json";
	var configurationPayload = {
		name: "Prometheus",
		type: "prometheus",
		url: "http://prometheus-configured:9090",
		access: "proxy",
		isDefault: true
	};
	configurationPayload = JSON.stringify(configurationPayload).replace(/"/g, "\\\"");
	new Command('./', ['docker', 'exec', '-i', 'grafana-configured', '/bin/sh', '-c', "curl '" + prometheusDatasourcesEndpoint + "' -H '" + configurationRequestHeaders + "' -d '" + configurationPayload + "'"]).execute();
}

function importDashboardToGrafana() {
	var prometheusDashboardsEndpoint = "http://admin:admin@localhost:3000/api/dashboards/db";
	var configurationRequestHeaders = "Content-Type: application/json";
	new Command('./', ['docker', 'exec', '-i', 'grafana-configured', '/bin/sh', '-c', "curl '" + prometheusDashboardsEndpoint + "' -H '" + configurationRequestHeaders + "' -d @/uuid_generation_performance.json"]).execute();
}

function cleanCompileAndDeploy() {
	new Command('../', 'mvn clean install -P wildfly-local').execute();
}
