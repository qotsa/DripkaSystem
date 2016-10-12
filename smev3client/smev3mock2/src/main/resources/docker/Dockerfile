FROM frolvlad/alpine-oraclejdk8:slim

ADD ${artifactId}-${version}-bootable.jar ${artifactId}.jar
RUN mkdir ftpFiles

COPY ftpFiles ftpFiles
RUN sh -c 'touch /${artifactId}.jar'

ENTRYPOINT [${jvm.args}, "${artifactId}.jar"]

EXPOSE ${smev3mock2.http.port} ${smev3mock2.ftp.port} ${debug.port}