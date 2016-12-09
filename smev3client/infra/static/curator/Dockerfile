FROM alpine:3.2

RUN apk --update add python py-pip && \
    pip install elasticsearch-curator==4.2.1 && \
    rm -rf /var/cache/apk/*

ADD entrypoint.sh /entrypoint.sh

WORKDIR /usr/share/curator
COPY config ./config

RUN chmod +x /entrypoint.sh

#run every minute
ENV CRON 0 23 * * *
ENV ES_HOST elasticsearch
ENV CONFIG_FILE /usr/share/curator/config/curator.yml
ENV COMMAND /usr/share/curator/config/delete_old_data.yml

ENTRYPOINT ["/entrypoint.sh"]