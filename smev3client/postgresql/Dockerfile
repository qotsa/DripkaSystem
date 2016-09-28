FROM postgres:9.6

COPY init/01-activiti.sh /docker-entrypoint-initdb.d/01-activiti.sh

ENV POSTGRES_PASSWORD "postgres"
ENV POSTGRES_USER "postgres"
ENV PGDATA "/var/lib/postgresql/data/pgdata"