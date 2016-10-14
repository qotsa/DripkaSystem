FROM stilliard/pure-ftpd:hardened

RUN mkdir /etc/pure-ftpd/users
RUN ((echo smev3clientpassword; echo smev3clientpassword) | pure-pw useradd smev3client -u ftpuser -d /home/ftpusers/smev3client); pure-pw mkdb /etc/pure-ftpd/users/pureftpd.pdb

CMD /usr/sbin/pure-ftpd -c 30 -C 10 -l puredb:/etc/pure-ftpd/users/pureftpd.pdb -E -j -R -P $PUBLICHOST -S 3333 -p 30010:30019

EXPOSE 3333 30010-30019
