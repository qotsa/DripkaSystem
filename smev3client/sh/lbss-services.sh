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



operation=${1}
shift

case $operation in
    clean)
        printf "Removing all the services and infrastructure"
        docker service rm lbss, lbss-db
		docker volume rm lbssLOG lbssFTP lbssDB
	
        ;;

	create)
	
		log1 "Docker infrastructure section"
		docker network create -d overlay smev3client
		
		log1 "Creating named volumes"
		docker volume create --name lbssLOG
		docker volume create --name lbssFTP
		docker volume create --name lbssDB

		sleep 1
		log1 "Infrastructure services section"
		echoProgress

		log2 "*******Creating lbss-db service*******"
		docker service create --name lbss-db --network smev3client --replicas 1 -p 27017:27017 --mount type=volume,source=lbssDB,destination=/data/db localhost:5000/lbss-db

		sleep 1
		log1 "END of infrastructure services section"
		sleep 1

		log1 "Creating business services"
		echoProgress

		log2 "*******Creating lbss service*******"
		docker service create --name lbss --network smev3client --replicas 1 -p 8080:8080 -p 2121:2121 --mount type=volume,source=lbssLOG,destination=/var/log/lbss --mount type=volume,source=lbssFTP,destination=/data/ftp --log-opt max-size=10m --log-opt max-file=10 localhost:5000/lbss-sba

		echoProgress
		log1 "Existing networks:"
		docker network ls

		log1 "Existing services:"
		docker service ls | grep -G -i 'localhost.*lbss.*'

		log1 "Existing volumes:"
		docker volume ls | grep -G -i 'lbss.*'
		sleep 1
		log1 "THE END"
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