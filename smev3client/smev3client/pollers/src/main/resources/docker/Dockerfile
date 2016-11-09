FROM frolvlad/alpine-oraclejdk8:slim

ADD ${artifactId}-${version}-bootable.jar ${artifactId}.jar

RUN sh -c 'touch /${artifactId}.jar'

ENTRYPOINT [${jvm.args},"/${artifactId}.jar"]

EXPOSE ${pollers.http.port} ${pollers.hazelcast.port} ${debug.port} ${profile.port}
