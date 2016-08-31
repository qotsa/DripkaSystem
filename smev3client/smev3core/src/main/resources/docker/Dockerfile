FROM frolvlad/alpine-oraclejdk8:slim

ADD ${artifactId}-${version}.jar ${artifactId}.jar
RUN sh -c 'touch /{artifactId}.jar'

ENTRYPOINT [${jvm.args}, "${artifactId}.jar"]

EXPOSE ${smev3core.http.port} ${debug.port}