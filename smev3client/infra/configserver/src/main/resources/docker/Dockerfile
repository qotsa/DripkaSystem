FROM frolvlad/alpine-oraclejdk8:slim

ADD ${artifactId}-${version}-bootable.jar ${artifactId}.jar

RUN sh -c 'touch /${artifactId}.jar'

ENTRYPOINT ["java","-jar","/${artifactId}.jar"]

EXPOSE ${configserver.http.port}