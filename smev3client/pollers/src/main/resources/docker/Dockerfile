FROM frolvlad/alpine-oraclejdk8:slim
ADD pollers-${version}.jar pollers.jar
RUN sh -c 'touch /pollers.jar'
ENTRYPOINT ["java","-jar","/pollers.jar"]
EXPOSE ${pollers.host.port} ${pollers.wtf.port}
