#!/bin/bash

./stop_swarm

yes | docker-machine rm master
yes | docker-machine rm worker1
yes | docker-machine rm worker2