FROM frolvlad/alpine-oraclejdk8:slim

ADD ${artifactId}-${version}-bootable.jar ${artifactId}.jar
RUN sh -c 'touch /${artifactId}.jar'

RUN mkdir /replication_cache

ENTRYPOINT [${jvm.args}, "${artifactId}.jar"]

EXPOSE ${replication.http.port} ${debug.port} ${profile.port}