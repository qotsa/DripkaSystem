FROM logstash:latest

# add custom config
COPY logstash.conf /config-dir/logstash.conf

RUN /opt/logstash/bin/logstash-plugin install logstash-filter-de_dot

EXPOSE 12201 4560