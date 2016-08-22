FROM frolvlad/alpine-oraclejdk8:slim
ADD pollers-0.0.1-SNAPSHOT.jar pollers.jar
RUN sh -c 'touch /pollers.jar'
ENTRYPOINT ["java","-jar","/pollers.jar"]
EXPOSE 8092 5701
