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
SERVICE_CONSTRAINTS_INFRA="--constraint node.labels.nodeType==worker --constraint node.labels.appType==infra"
SERVICE_CONSTRAINTS_BUSINESS="--constraint node.labels.nodeType==worker --constraint node.labels.appType==business"
#SERVICE_CONSTRAINTS_INFRA=""
#SERVICE_CONSTRAINTS_BUSINESS=""

declare -A SERVICES_INFRA
SERVICES_INFRA[activemq]="-p 8161:8161 -p 61616:61616 -p 61613:61613 --env \"ACTIVEMQ_MIN_MEMORY=512\" --env \"ACTIVEMQ_MAX_MEMORY=2048\" --env \"ACTIVEMQ_NAME=amqp-srv1\" --env \"ACTIVEMQ_REMOVE_DEFAULT_ACCOUNT=true\" --env \"ACTIVEMQ_ADMIN_LOGIN=admin\" --env \"ACTIVEMQ_ADMIN_PASSWORD=admin\" --env \"ACTIVEMQ_WRITE_LOGIN=producer\" --env \"ACTIVEMQ_WRITE_PASSWORD=producer\" --env \"ACTIVEMQ_READ_LOGIN=consumer\" --env \"ACTIVEMQ_READ_PASSWORD=consumer\" --env \"ACTIVEMQ_JMX_LOGIN=jmx\" --env \"ACTIVEMQ_JMX_PASSWORD=jmx\" --env \"ACTIVEMQ_ENABLED_SCHEDULER=true\" ${REGISTRY}/activemq:${IMAGES_VERSION}"
SERVICES_INFRA[ftp]="-p 2021:2021 -p 30000-30059:30000-30059 --mount type=volume,source=ftpData,destination=/home/ftpusers --mount type=volume,source=ftpUsers,destination=/etc/pure-ftpd/users --env \"PUBLICHOST=ftp\" ${REGISTRY}/ftp:${IMAGES_VERSION}"
SERVICES_INFRA[ftpsmev]="-p 3333:3333 -p 30060-30119:30060-30119 --mount type=volume,source=ftpSmevData,destination=/home/ftpusers --mount type=volume,source=ftpSmevUsers,destination=/etc/pure-ftpd/users --env \"PUBLICHOST=ftpsmev\" ${REGISTRY}/ftpsmev:${IMAGES_VERSION}"
#SERVICES_INFRA[postgresql]="-p 5432:5432 --mount type=volume,source=postgresData,destination=/var/lib/postgresql/data ${REGISTRY}/postgresql:${IMAGES_VERSION}"
SERVICES_INFRA[elasticsearch]="-p 9200:9200 --mount type=volume,source=elasticsearchData,destination=/usr/share/elasticsearch/data ${REGISTRY}/elasticsearch:${IMAGES_VERSION}"
#SERVICES_INFRA[elasticsearch-head]="-p 9100:9100 ${REGISTRY}/elasticsearch-head:${IMAGES_VERSION}"
SERVICES_INFRA[logstash]="-p 12201:12201 -p 4560:4560 ${REGISTRY}/logstash:${IMAGES_VERSION} logstash -f /config-dir/logstash.conf"
SERVICES_INFRA[curator]="${REGISTRY}/curator:${IMAGES_VERSION}"
#SERVICES_INFRA[elastalert]="${REGISTRY}/elastalert:${IMAGES_VERSION}"
SERVICES_INFRA[cadvisor]="-p 8081:8081 --mount type=bind,source=/../,destination=/rootfs:ro --mount type=bind,source=/var/run,destination=/var/run:rw --mount type=bind,source=/sys,destination=/sys:ro --mount type=bind,source=/var/lib/docker/,destination=/var/lib/docker:ro ${REGISTRY}/cadvisor:${IMAGES_VERSION} -port=8081"
SERVICES_INFRA[kibana]="-p 5601:5601 --env \"--max-old-space-size=250\" ${REGISTRY}/kibana:${IMAGES_VERSION}"
#SERVICES_INFRA[grafana]="-p 3000:3000 ${REGISTRY}/grafana:${IMAGES_VERSION}"
#SERVICES_INFRA[bpmengine]="-p 8098:8098 ${REGISTRY}/bpmengine:${IMAGES_VERSION}"

declare -A SERVICES
SERVICES[eureka]="-p 8097:8097 ${REGISTRY}/eureka:${IMAGES_VERSION}"
#SERVICES[hystrix]="-p 8099:8099 ${REGISTRY}/hystrix:${IMAGES_VERSION}"
#SERVICES[configserver]="-p 8888:8888 ${REGISTRY}/configserver:${IMAGES_VERSION}"
SERVICES[smev3core]="-p 8093:8093 ${REGISTRY}/smev3core:${IMAGES_VERSION}"
SERVICES[smev3adapter]="-p 8090:8090 ${REGISTRY}/smev3adapter:${IMAGES_VERSION}"
SERVICES[ufosadapter]="-p 8094:8094 ${REGISTRY}/ufosadapter:${IMAGES_VERSION}"
SERVICES[replication]="-p 8095:8095 ${REGISTRY}/replication:${IMAGES_VERSION}"
SERVICES[pollers]="-p 8092:8092 -p 5701:5701 ${REGISTRY}/pollers:${IMAGES_VERSION}"
SERVICES[pushers]="-p 8096:8096 ${REGISTRY}/pushers:${IMAGES_VERSION}"
SERVICES[smev3mock2]="-p 8091:8091 ${REGISTRY}/smev3mock2:${IMAGES_VERSION}"

VOLUMES=("ftpData" "ftpUsers" "ftpSmevData" "ftpSmevUsers" "postgresData" "elasticsearchData")

operation=${1}
shift

case $operation in
    clean)
        printf "Removing all the services and infrastructure\n"
        docker service rm $(docker service ls -q)
        sleep 2

        for volume in "${VOLUMES[@]}"
        do
           docker volume rm ${volume}
        done

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
           docker service create ${SERVICE_CONSTRAINTS_INFRA} ${SERVICE_PARAMS_COMMON} --name ${service} ${SERVICES_INFRA[${service}]}
        done

        sleep 1
        log1 "END of infrastructure services section"

        sleep 1
        log1 "Business services section"
        echoProgress

        for service in "${!SERVICES[@]}"
        do
           docker service create ${SERVICE_CONSTRAINTS_BUSINESS} ${SERVICE_PARAMS_COMMON} --name ${service} ${SERVICES[${service}]}
        done

        ./swarm-info.sh

        sleep 1
        log1 "THE END"
    ;;

    pull)
        printf "Pulling images\n"

        for service in "${!SERVICES_INFRA[@]}"
        do
           docker pull ${REGISTRY}/${service}:${IMAGES_VERSION}
        done

        for service in "${!SERVICES[@]}"
        do
           docker pull ${REGISTRY}/${service}:${IMAGES_VERSION}
        done
    ;;

    scale_down)
        printf "Scaling down services and infrastructure\n"

        for service in "${!SERVICES_INFRA[@]}"
        do
           docker service scale ${service}=0
        done

        for service in "${!SERVICES[@]}"
        do
           docker service scale ${service}=0
        done
    ;;

    scale_up)
        printf "Scaling up services and infrastructure\n"

        for service in "${!SERVICES_INFRA[@]}"
        do
           docker service scale ${service}=1
        done

        for service in "${!SERVICES[@]}"
        do
           docker service scale ${service}=1
        done
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





