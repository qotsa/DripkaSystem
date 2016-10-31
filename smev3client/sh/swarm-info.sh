#!/bin/bash

log1() {
	echo -e "\\n=============$1=============\\n"
}

log1 "Existing networks:"
docker network ls

log1 "Existing services:"
docker service ls

log1 "Existing volumes:"
docker volume ls
