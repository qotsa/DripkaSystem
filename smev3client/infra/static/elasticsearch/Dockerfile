FROM elasticsearch:5.0.2

RUN echo "" >> /usr/share/elasticsearch/config/elasticsearch.yml && \
    echo "http.cors.enabled: true" >> /usr/share/elasticsearch/config/elasticsearch.yml && \
    echo "http.cors.allow-origin: \"*\"" >> /usr/share/elasticsearch/config/elasticsearch.yml

EXPOSE 9200 9300