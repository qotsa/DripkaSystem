FROM frolvlad/alpine-oraclejdk8:slim

ADD ${artifactId}-${version}.jar ${artifactId}.jar
RUN sh -c 'touch /${artifactId}.jar'

#ENTRYPOINT ["java","-jar","/${artifactId}.jar"]
ENTRYPOINT [${jvm.args}]

EXPOSE ${ufosAdapter.host.port} ${debug.port}