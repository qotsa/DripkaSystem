FROM frolvlad/alpine-oraclejdk8:slim
ADD configserver-0.0.1-SNAPSHOT.jar configserver.jar
RUN sh -c 'touch /configserver.jar'
ENTRYPOINT ["java","-jar","/configserver.jar"]
EXPOSE 8888