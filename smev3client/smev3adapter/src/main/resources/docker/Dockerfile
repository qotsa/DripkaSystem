FROM frolvlad/alpine-oraclejdk8:slim

ADD smev3adapter-${version}.jar smev3adapter.jar
RUN sh -c 'touch /{artifactId}.jar'

#ENTRYPOINT ["java","-agentlib:jdwp=transport=dt_socket,server=y,suspend=y,address=5005","-jar","/smev3adapter.jar"]
ENTRYPOINT [${jvm.args}, "${artifactId}.jar"]
#ENTRYPOINT ["java","-jar","/smev3adapter.jar"]

EXPOSE ${smev3adapter.host.port} ${debug.port}