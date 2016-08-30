FROM frolvlad/alpine-oraclejdk8:slim

ADD smev3core-${version}.jar smev3core.jar
RUN sh -c 'touch /{artifactId}.jar'

ENTRYPOINT [${jvm.args}, "${artifactId}.jar"]

EXPOSE ${debug.port}