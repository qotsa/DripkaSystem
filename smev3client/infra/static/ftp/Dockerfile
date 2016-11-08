FROM stilliard/pure-ftpd:hardened

RUN mkdir /etc/pure-ftpd/users
RUN ((echo smev3clientpassword; echo smev3clientpassword) | pure-pw useradd smev3client -u ftpuser -d /home/ftpusers/smev3client); pure-pw mkdb /etc/pure-ftpd/users/pureftpd.pdb

CMD /usr/sbin/pure-ftpd -y 50:0 -c 50 -C 50 -l puredb:/etc/pure-ftpd/users/pureftpd.pdb -E -j -R -P $PUBLICHOST -S 2021 -p 30000-30049

EXPOSE 2021 30000-30049
