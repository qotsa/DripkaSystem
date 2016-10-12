FROM logstash:2.4.0-1

# add custom config
COPY logstash.conf /config-dir/logstash.conf

RUN /opt/logstash/bin/logstash-plugin install logstash-filter-de_dot

EXPOSE 12201 4560