#**smev3client**
## Description
Camel based http-to-soap adapter. Java - based + mixed config (*resources/config*)
Contains:
- embedded Tomcat for using servlet transport in Camel
- [joolokia][0] servlet for using [Hawtio] [1]
- actuator metrics
- yaml based config
- spring profiles
- autowired props
- new test runner
- tested @adviceWith and @MockEndpoints


##How to build
Maven profiles:
- build-docker-images - use in activation mode, builds images
- debug - adds jvm debug flags to entrypoin in docker image
- env - local  - uses filtering from env/local.properties for docker images and configuration files filtering


##How to run

There are main compose file in root project and compose files per each project, having only current service and 
its dependencies

Use spring boot plugin to run each service independently.
Use 
```
docker-compose up -d 
```
to run all the stuff.
All the images should be available for current docker daemon

##How to run hawtio console
Download executable jar from [Hawtio] [1] , start it and connect to jolokia agent using recomendations in welcome page.
Not forget to notice host/host managment port in spring config

##How to build docker image
Set env vars for remote/local building
E.g. 
```
DOCKER_TLS_VERIFY="1"
DOCKER_HOST="tcp://192.168.99.101:2376"
DOCKER_CERT_PATH="C:\Users\tartanov.mikhail\.docker\machine\machines\default"
DOCKER_MACHINE_NAME="default"
```
then build image using appropriate maven plugin

##How to debug
Use debug profile (enabled for smev3adapter)

##TODO
- logging agregator
- issues with one command build in maven
- refactor all hardcoded vals : hosts, ports and etc
- unit and route tests
- all the microservices stuff : curcuit bracer, balancer, service discovery etc

###Known issues with docker
- Unable to build image on swarm cluster using this plugin. Should use local repo for checking out pre build images when starting services on cluster.

[0] : https://jolokia.org
[1] : http://hawt.io/