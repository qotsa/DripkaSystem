FROM node:7.1

RUN npm install grunt -g && cd / && \
    git clone git://github.com/mobz/elasticsearch-head.git && \
    cd /elasticsearch-head && \
    npm install

COPY Gruntfile.js /elasticsearch-head/Gruntfile.js

WORKDIR /elasticsearch-head

ENTRYPOINT grunt server

EXPOSE 9100