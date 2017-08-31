#!/bin/bash
for SCRIPT in /docker-entrypoint-initmw.d/*.cli
do
	if [ -f $SCRIPT ]
	then
		$WILDFLY_HOME/bin/jboss-cli.sh --file=$SCRIPT
	fi
done
