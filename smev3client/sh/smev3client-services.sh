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

SERVICE_PARAMS_COMMON="--network smev3client --replicas 1 --log-opt max-size=10m --log-opt max-file=10 --with-registry-auth --restart-condition none"
SERVICE_CONSTRAINTS="--constraint node.labels.nodeType==worker"

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
        docker volume create --name ftpData
        docker volume create --name ftpUsers
        docker volume create --name ftpSmevData
        docker volume create --name ftpSmevUsers
        docker volume create --name postgresData
        docker volume create --name elasticsearchData

        sleep 1
        log1 "Infrastructure services section"
        echoProgress

        docker service create ${SERVICE_CONSTRAINTS} ${SERVICE_PARAMS_COMMON} --name activemq      -p 8161:8161 -p 61616:61616 -p 61613:61613 --env "ACTIVEMQ_MIN_MEMORY=512" --env "ACTIVEMQ_MAX_MEMORY=2048" --env "ACTIVEMQ_NAME=amqp-srv1" --env "ACTIVEMQ_REMOVE_DEFAULT_ACCOUNT=true" --env "ACTIVEMQ_ADMIN_LOGIN=admin" --env "ACTIVEMQ_ADMIN_PASSWORD=admin" --env "ACTIVEMQ_WRITE_LOGIN=producer" --env "ACTIVEMQ_WRITE_PASSWORD=producer" --env "ACTIVEMQ_READ_LOGIN=consumer" --env "ACTIVEMQ_READ_PASSWORD=consumer" --env "ACTIVEMQ_JMX_LOGIN=jmx" --env "ACTIVEMQ_JMX_PASSWORD=jmx" --env "ACTIVEMQ_ENABLED_SCHEDULER=true"  172.31.191.101:8123/activemq:1.0
        docker service create ${SERVICE_CONSTRAINTS} ${SERVICE_PARAMS_COMMON} --name ftp           -p 2021:2021 -p 30000-30009:30000-30009 --mount type=volume,source=ftpData,destination=/home/ftpusers --mount type=volume,source=ftpUsers,destination=/etc/pure-ftpd/users --env "PUBLICHOST=ftp" 172.31.191.101:8123/ftp:1.0
        docker service create ${SERVICE_CONSTRAINTS} ${SERVICE_PARAMS_COMMON} --name ftpsmev       -p 3333:3333 -p 30010-30019:30010-30019 --mount type=volume,source=ftpSmevData,destination=/home/ftpusers --mount type=volume,source=ftpSmevUsers,destination=/etc/pure-ftpd/users --env "PUBLICHOST=ftpsmev" 172.31.191.101:8123/ftpsmev:1.0
        docker service create ${SERVICE_CONSTRAINTS} ${SERVICE_PARAMS_COMMON} --name postgresql    -p 5432:5432 --mount type=volume,source=postgresData,destination=/var/lib/postgresql/data 172.31.191.101:8123/postgresql:1.0
        docker service create ${SERVICE_CONSTRAINTS} ${SERVICE_PARAMS_COMMON} --name elasticsearch -p 9200:9200 --mount type=volume,source=elasticsearchData,destination=/usr/share/elasticsearch/data 172.31.191.101:8123/elasticsearch:1.0
        docker service create ${SERVICE_CONSTRAINTS} ${SERVICE_PARAMS_COMMON} --name logstash      -p 12201:12201 -p 4560:4560 172.31.191.101:8123/logstash:1.0 logstash -f /config-dir/logstash.conf
        docker service create ${SERVICE_CONSTRAINTS} ${SERVICE_PARAMS_COMMON} --name curator       172.31.191.101:8123/curator:1.0
        docker service create ${SERVICE_CONSTRAINTS} ${SERVICE_PARAMS_COMMON} --name cadvisor      -p 8081:8081 --mount type=bind,source=/../,destination=/rootfs:ro --mount type=bind,source=/var/run,destination=/var/run:rw --mount type=bind,source=/sys,destination=/sys:ro --mount type=bind,source=/var/lib/docker/,destination=/var/lib/docker:ro 172.31.191.101:8123/cadvisor:1.0 -port=8081
        docker service create ${SERVICE_CONSTRAINTS} ${SERVICE_PARAMS_COMMON} --name kibana        -p 5601:5601 --env "--max-old-space-size=250" 172.31.191.101:8123/kibana:1.0
        docker service create ${SERVICE_CONSTRAINTS} ${SERVICE_PARAMS_COMMON} --name grafana       -p 3000:3000 172.31.191.101:8123/grafana:1.0

        sleep 1
        log1 "END of infrastructure services section"

        sleep 1
        log1 "Spring cloud infrastructure services section"
        echoProgress

        docker service create ${SERVICE_CONSTRAINTS} ${SERVICE_PARAMS_COMMON} --name eureka       -p 8097:8097 172.31.191.101:8123/eureka:1.0
        docker service create ${SERVICE_CONSTRAINTS} ${SERVICE_PARAMS_COMMON} --name configserver -p 8888:8888 172.31.191.101:8123/configserver:1.0
        docker service create ${SERVICE_CONSTRAINTS} ${SERVICE_PARAMS_COMMON} --name smev3core    -p 8093:8093 172.31.191.101:8123/smev3core:1.0
        docker service create ${SERVICE_CONSTRAINTS} ${SERVICE_PARAMS_COMMON} --name smev3adapter -p 8090:8090 172.31.191.101:8123/smev3adapter:1.0
        docker service create ${SERVICE_CONSTRAINTS} ${SERVICE_PARAMS_COMMON} --name ufosadapter  -p 8094:8094 172.31.191.101:8123/ufosadapter:1.0
        docker service create ${SERVICE_CONSTRAINTS} ${SERVICE_PARAMS_COMMON} --name replication  -p 8095:8095 172.31.191.101:8123/replication:1.0
        docker service create ${SERVICE_CONSTRAINTS} ${SERVICE_PARAMS_COMMON} --name pollers      -p 8092:8092 -p 5701:5701 172.31.191.101:8123/pollers:1.0
        docker service create ${SERVICE_CONSTRAINTS} ${SERVICE_PARAMS_COMMON} --name pushers      -p 8096:8096 172.31.191.101:8123/pushers:1.0
        docker service create ${SERVICE_CONSTRAINTS} ${SERVICE_PARAMS_COMMON} --name smev3mock2   -p 8091:8091 172.31.191.101:8123/smev3mock2:1.0
        docker service create ${SERVICE_CONSTRAINTS} ${SERVICE_PARAMS_COMMON} --name bpmengine    -p 8098:8098 172.31.191.101:8123/bpmengine:1.0

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

    scale_down)
        printf "Scaling down services and infrastructure\n"

        docker service scale activemq=0
        docker service scale ftp=0
        docker service scale ftpsmev=0
        docker service scale postgresql=0
        docker service scale elasticsearch=0
        docker service scale logstash=0
        docker service scale curator=0
        docker service scale cadvisor=0
        docker service scale kibana=0
        docker service scale grafana=0

        docker service scale eureka=0
        docker service scale configserver=0
        docker service scale smev3core=0
        docker service scale smev3adapter=0
        docker service scale ufosadapter=0
        docker service scale replication=0
        docker service scale pollers=0
        docker service scale pushers=0
        docker service scale smev3mock2=0
        docker service scale bpmengine=0
        ;;

    scale_up)
        printf "Scaling up services and infrastructure\n"

        docker service scale activemq=1
        docker service scale ftp=1
        docker service scale ftpsmev=1
        docker service scale postgresql=1
        docker service scale elasticsearch=1
        docker service scale logstash=1
        docker service scale curator=1
        docker service scale cadvisor=1
        docker service scale kibana=1
        docker service scale grafana=1

        docker service scale eureka=1
        docker service scale configserver=1
        docker service scale smev3core=1
        docker service scale smev3adapter=1
        docker service scale ufosadapter=1
        docker service scale replication=1
        docker service scale pollers=0
        docker service scale pushers=1
        docker service scale smev3mock2=1
        docker service scale bpmengine=1
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





