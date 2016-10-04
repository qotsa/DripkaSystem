FROM frolvlad/alpine-oraclejdk8:full

ADD ${artifactId}-${version}-bootable.jar ${artifactId}.jar
RUN sh -c 'touch /${artifactId}.jar'

ENTRYPOINT [${jvm.args}, "${artifactId}.jar"]

EXPOSE ${bpmengine.http.port} ${debug.port}