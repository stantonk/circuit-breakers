#!/bin/sh
java -jar testservice-1.0.0.jar server config.yml &
/usr/local/bin/envoy -c /etc/envoy.yaml --service-cluster "${BACK_SERVICE_HOSTNAME}"