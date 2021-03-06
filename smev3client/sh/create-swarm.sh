#!/bin/bash

log() {
	echo "=== $1"
}

# create swarm master
log "Creating Swarm token"
#--engine-insecure-registry
docker-machine create -d virtualbox master

eval "$(docker-machine env master)"

./docker-machine-ipconfig static master 192.168.99.98
docker-machine restart master
yes | docker-machine regenerate-certs master

eval "$(docker-machine env master)"
docker swarm init --advertise-addr $(docker-machine ip master)

SWAM_TOKEN_WORKER_DIRTY=$(docker swarm join-token worker)
SWAM_TOKEN_MANAGER_DIRTY=$(docker swarm join-token manager)
SWARM_TOKEN_WORKER=${SWAM_TOKEN_WORKER_DIRTY#"To add a worker to this swarm, run the following command:"} 
SWARM_TOKEN_MANAGER=${SWAM_TOKEN_MANAGER_DIRTY#"To add a manager to this swarm, run the following command:"} 

log "SWARM_WORKER token is: $SWARM_TOKEN_WORKER"
log "SWARM_MANAGER token is: $SWARM_TOKEN_MANAGER"

# create Swarm worker1
#log "Creating Swarm worker1"
#docker-machine create -d virtualbox worker1
#eval "$(docker-machine env worker1)"
#connect to cluster
#eval "($SWARM_TOKEN_WORKER)"

# create Swarm worker2
#log "Creating Swarm worker2"
#docker-machine create -d virtualbox worker2
#eval "$(docker-machine env worker2)"
#connect to cluster
#eval "($SWARM_TOKEN_WORKER)"

eval "$(docker-machine env master)"
