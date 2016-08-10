FROM frolvlad/alpine-oraclejdk8:slim
ADD smev3adapter-0.0.1-SNAPSHOT.jar smev3adapter.jar
RUN sh -c 'touch /smev3adapter.jar'
ENTRYPOINT ["java","-jar","/smev3adapter.jar"]
EXPOSE 8090