FROM frolvlad/alpine-oraclejdk8:slim
ADD configserver-${version}.jar configserver.jar
RUN sh -c 'touch /configserver.jar'
ENTRYPOINT ["java","-jar","/configserver.jar"]
EXPOSE ${configserver.host.port}