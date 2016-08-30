FROM frolvlad/alpine-oraclejdk8:slim

ADD smev3core-${version}.jar smev3core.jar
RUN sh -c 'touch /smev3core.jar'

#ENTRYPOINT ["java","-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=5005","-jar","/smev3core.jar"]
ENTRYPOINT ["java","-jar","/smev3core.jar"]
