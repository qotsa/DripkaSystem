FROM debian:jessie

# feel free to change this ;)
MAINTAINER DripTech

# properly setup debian sources
ENV DEBIAN_FRONTEND noninteractive
RUN echo "deb http://http.debian.net/debian jessie main\n\
deb-src http://http.debian.net/debian jessie main\n\
deb http://http.debian.net/debian jessie-updates main\n\
deb-src http://http.debian.net/debian jessie-updates main\n\
deb http://security.debian.org jessie/updates main\n\
deb-src http://security.debian.org jessie/updates main\n\
" > /etc/apt/sources.list
RUN apt-get -y update

# install package building helpers
RUN apt-get -y --force-yes install dpkg-dev debhelper

# install dependancies
RUN apt-get -y build-dep pure-ftpd

# build from source
RUN mkdir /tmp/pure-ftpd/ && \
	cd /tmp/pure-ftpd/ && \
	apt-get source pure-ftpd && \
	cd pure-ftpd-* && \
	sed -i '/^optflags=/ s/$/ --without-capabilities/g' ./debian/rules && \
	dpkg-buildpackage -b -uc

# install the new deb files
RUN dpkg -i /tmp/pure-ftpd/pure-ftpd-common*.deb
RUN apt-get -y install openbsd-inetd
RUN dpkg -i /tmp/pure-ftpd/pure-ftpd_*.deb

# Prevent pure-ftpd upgrading
RUN apt-mark hold pure-ftpd pure-ftpd-common

# setup ftpgroup and ftpuser
RUN groupadd ftpgroup
RUN useradd -g ftpgroup -d /home/ftpusers -s /dev/null ftpuser

ENV PUBLICHOST ftpsmev

VOLUME ["/home/ftpusers", "/etc/pure-ftpd/passwd"]

RUN mkdir /etc/pure-ftpd/users
RUN ((echo smev3clientpassword; echo smev3clientpassword) | pure-pw useradd smev3client -u ftpuser -d /home/ftpusers/smev3client); pure-pw mkdb /etc/pure-ftpd/users/pureftpd.pdb

CMD /usr/sbin/pure-ftpd -y 60:0 -c 60 -C 60 -l puredb:/etc/pure-ftpd/users/pureftpd.pdb -E -j -R -P $PUBLICHOST -S 3333 -p 30060:30119

EXPOSE 3333 30060-30119
