FROM frolvlad/alpine-oraclejdk8:slim
ADD smev3mock-${version}.jar smev3mock.jar
RUN sh -c 'touch /smev3mock.jar'
ENTRYPOINT ["java","-jar","/smev3mock.jar"]
EXPOSE ${smev3mock.http.port}