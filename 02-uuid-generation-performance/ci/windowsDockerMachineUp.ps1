$machineName = "jee-docker-machine"

$dockerMachines = docker-machine ls 
if ($dockerMachines -Like "*$machineName*") {
	echo "Starting docker machine..."
	docker-machine start $machineName
	docker-machine env $machineName
	&docker-machine env $machineName | Invoke-Expression
	echo "Docker machine started."
} else {
	echo "Creating docker machine..."
	docker-machine create --driver virtualbox --virtualbox-disk-size "5000" --virtualbox-memory "8192" --virtualbox-cpu-count "4" $machineName
	docker-machine start $machineName
	docker-machine env $machineName
	&docker-machine env $machineName | Invoke-Expression
	echo "Docker machine created."

	echo "Setting ports forwarding on Oracle VirtualBox machine..."
	&"C:\Program Files\Oracle\VirtualBox\VBoxManage" controlvm "$machineName" natpf1 "Postgres,tcp,,5432,,5432"
	&"C:\Program Files\Oracle\VirtualBox\VBoxManage" controlvm "$machineName" natpf1 "Wildfly HTTP,tcp,,8080,,8080"
	&"C:\Program Files\Oracle\VirtualBox\VBoxManage" controlvm "$machineName" natpf1 "Wildfly Remote Debugging,tcp,,8787,,8787"
	&"C:\Program Files\Oracle\VirtualBox\VBoxManage" controlvm "$machineName" natpf1 "Wildfly Admin Console,tcp,,9990,,9990"
	echo "Setting ports forwarding finished."
	echo ""
}