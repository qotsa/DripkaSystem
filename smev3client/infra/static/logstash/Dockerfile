FROM logstash:5.0.0

COPY logstash.conf /config-dir/logstash.conf

RUN /usr/share/logstash/bin/logstash-plugin install logstash-filter-de_dot && mkdir /templates

COPY templates/business.json /templates/business.json
COPY templates/camel_tracer.json /templates/camel_tracer.json
COPY templates/metrics.json /templates/metrics.json
COPY templates/raw.json /templates/raw.json

EXPOSE 12201 4560