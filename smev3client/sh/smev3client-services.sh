#!/bin/bash

log1() {
    echo -e "\\n=============$1=============\\n"
}

log2() {
    echo -e "\\n*******$1*******\\n"
}

echoProgress() {
    echo -ne '#####                     (33%)\r'
    sleep 1
    echo -ne '#############             (66%)\r'
    sleep 1
    echo -ne '#######################   (100%)\r'
    echo -ne '\n'
}

REGISTRY=172.31.191.101:8123
IMAGES_VERSION=1.0
SERVICE_PARAMS_COMMON="--network smev3client --replicas 1 --log-opt max-size=10m --log-opt max-file=10 --with-registry-auth --restart-condition none"
SERVICE_CONSTRAINTS="--constraint node.labels.nodeType==worker"

#SERVICES=("eureka" "configserver" "smev3core" "smev3adapter" "ufosadapter" "replication" "pollers" "pushers" "smev3mock2" "bpmengine")
SERVICES=("eureka" "configserver" "smev3core" "smev3adapter" "ufosadapter" "replication" "pushers" "smev3mock2" "bpmengine")

declare -A SERVICES_INFRA
SERVICES_INFRA[activemq]="-p 8161:8161 -p 61616:61616 -p 61613:61613 --env \"ACTIVEMQ_MIN_MEMORY=512\" --env \"ACTIVEMQ_MAX_MEMORY=2048\" --env \"ACTIVEMQ_NAME=amqp-srv1\" --env \"ACTIVEMQ_REMOVE_DEFAULT_ACCOUNT=true\" --env \"ACTIVEMQ_ADMIN_LOGIN=admin\" --env \"ACTIVEMQ_ADMIN_PASSWORD=admin\" --env \"ACTIVEMQ_WRITE_LOGIN=producer\" --env \"ACTIVEMQ_WRITE_PASSWORD=producer\" --env \"ACTIVEMQ_READ_LOGIN=consumer\" --env \"ACTIVEMQ_READ_PASSWORD=consumer\" --env \"ACTIVEMQ_JMX_LOGIN=jmx\" --env \"ACTIVEMQ_JMX_PASSWORD=jmx\" --env \"ACTIVEMQ_ENABLED_SCHEDULER=true\""
SERVICES_INFRA[ftp]="-p 2021:2021 -p 30000-30009:30000-30009 --mount type=volume,source=ftpData,destination=/home/ftpusers --mount type=volume,source=ftpUsers,destination=/etc/pure-ftpd/users --env \"PUBLICHOST=ftp\""
SERVICES_INFRA[ftpsmev]="-p 3333:3333 -p 30010-30019:30010-30019 --mount type=volume,source=ftpSmevData,destination=/home/ftpusers --mount type=volume,source=ftpSmevUsers,destination=/etc/pure-ftpd/users --env \"PUBLICHOST=ftpsmev\""
SERVICES_INFRA[postgresql]="-p 5432:5432 --mount type=volume,source=postgresData,destination=/var/lib/postgresql/data"
SERVICES_INFRA[elasticsearch]="-p 9200:9200 --mount type=volume,source=elasticsearchData,destination=/usr/share/elasticsearch/data"
SERVICES_INFRA[logstash]="-p 12201:12201 -p 4560:4560"
SERVICES_INFRA[curator]=""
SERVICES_INFRA[cadvisor]="-p 8081:8081 --mount type=bind,source=/../,destination=/rootfs:ro --mount type=bind,source=/var/run,destination=/var/run:rw --mount type=bind,source=/sys,destination=/sys:ro --mount type=bind,source=/var/lib/docker/,destination=/var/lib/docker:ro"
SERVICES_INFRA[kibana]="-p 5601:5601 --env \"--max-old-space-size=250\""
SERVICES_INFRA[grafana]="-p 3000:3000"

VOLUMES=("ftpData" "ftpUsers" "ftpSmevData" "ftpSmevUsers" "postgresData" "elasticsearchData")

operation=${1}
shift

case $operation in
    clean)
        printf "Removing all the services and infrastructure\n"
        docker service rm $(docker service ls -q)
        sleep 2
        docker volume rm ftpData ftpUsers ftpSmevData ftpSmevUsers postgresData elasticsearchData
        docker network rm smev3client
        ;;

    create)
        log1 "Docker infrastructure section"
        log1 "Creating an overlay network named smev3client and named volumes"
        log1 "Creating overlay network"
        docker network create -d overlay smev3client

        sleep 1
        log1 "Creating named volumes"

        for volume in "${VOLUMES[@]}"
        do
           docker volume create --name ${volume}
        done

        sleep 1
        log1 "Infrastructure services section"
        echoProgress

        for service in "${!SERVICES_INFRA[@]}"
        do
           echo docker service create ${SERVICE_CONSTRAINTS} ${SERVICE_PARAMS_COMMON} --name ${service} ${SERVICES_INFRA[${service}]} ${REGISTRY}/${service}:${IMAGES_VERSION}
        done

        sleep 1
        log1 "END of infrastructure services section"

        sleep 1
        log1 "Spring cloud infrastructure services section"
        echoProgress

        #docker service create ${SERVICE_CONSTRAINTS} ${SERVICE_PARAMS_COMMON} --name eureka       -p 8097:8097 ${REGISTRY}/eureka:${IMAGES_VERSION}
        #docker service create ${SERVICE_CONSTRAINTS} ${SERVICE_PARAMS_COMMON} --name configserver -p 8888:8888 ${REGISTRY}/configserver:${IMAGES_VERSION}
        #docker service create ${SERVICE_CONSTRAINTS} ${SERVICE_PARAMS_COMMON} --name smev3core    -p 8093:8093 ${REGISTRY}/smev3core:${IMAGES_VERSION}
        #docker service create ${SERVICE_CONSTRAINTS} ${SERVICE_PARAMS_COMMON} --name smev3adapter -p 8090:8090 ${REGISTRY}/smev3adapter:${IMAGES_VERSION}
        #docker service create ${SERVICE_CONSTRAINTS} ${SERVICE_PARAMS_COMMON} --name ufosadapter  -p 8094:8094 ${REGISTRY}/ufosadapter:${IMAGES_VERSION}
        #docker service create ${SERVICE_CONSTRAINTS} ${SERVICE_PARAMS_COMMON} --name replication  -p 8095:8095 ${REGISTRY}/replication:${IMAGES_VERSION}
        #docker service create ${SERVICE_CONSTRAINTS} ${SERVICE_PARAMS_COMMON} --name pollers      -p 8092:8092 -p 5701:5701 ${REGISTRY}/pollers:${IMAGES_VERSION}
        #docker service create ${SERVICE_CONSTRAINTS} ${SERVICE_PARAMS_COMMON} --name pushers      -p 8096:8096 ${REGISTRY}/pushers:${IMAGES_VERSION}
        #docker service create ${SERVICE_CONSTRAINTS} ${SERVICE_PARAMS_COMMON} --name smev3mock2   -p 8091:8091 ${REGISTRY}/smev3mock2:${IMAGES_VERSION}
        #docker service create ${SERVICE_CONSTRAINTS} ${SERVICE_PARAMS_COMMON} --name bpmengine    -p 8098:8098 ${REGISTRY}/bpmengine:${IMAGES_VERSION}

        echoProgress
        log1 "Existing networks:"
        docker network ls

        log1 "Existing services:"
        docker service ls

        log1 "Existing volumes:"
        docker volume ls
        sleep 1
        log1 "THE END"
        ;;

    pull)
        printf "Pulling images\n"

        for service in "${!SERVICES_INFRA[@]}"
        do
           docker pull ${REGISTRY}/${service}:${IMAGES_VERSION}
        done

        #for service in "${SERVICES[@]}"
        #do
        #   docker pull ${REGISTRY}/${service}:${IMAGES_VERSION}
        #done
        ;;

    scale_down)
        printf "Scaling down services and infrastructure\n"

        for service in "${!SERVICES_INFRA[@]}"
        do
           docker service scale ${service}=0
        done

        #for service in "${SERVICES[@]}"
        #do
        #   docker service scale ${service}=0
        #done
        ;;

    scale_up)
        printf "Scaling up services and infrastructure\n"

        for service in "${!SERVICES_INFRA[@]}"
        do
           docker service scale ${service}=1
        done

        #for service in "${SERVICES[@]}"
        #do
        #   docker service scale ${service}=1
        #done
        ;;

    *)
        printf "Usage: $(basename $BASH_SOURCE) <command> args...\n"
        printf "\n"
        printf "Commands:\n"
        printf "    create			Create smev3client services and infrastructure\n"
        printf "\n"
        printf "    clean			Clean smev3client services and infrastructure\n"
    ;;
esac





