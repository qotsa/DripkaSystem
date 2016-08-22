FROM frolvlad/alpine-oraclejdk8:slim
ADD smev3mock-0.0.1-SNAPSHOT.jar smev3mock.jar
RUN sh -c 'touch /smev3mock.jar'
ENTRYPOINT ["java","-jar","/smev3mock.jar"]
EXPOSE 8091