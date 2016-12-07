#!/bin/sh

CRON=${CRON:-"0 1 * * *"}
ES_HOST=${ES_HOST:-"elasticsearch"}

echo "$CRON /usr/bin/curator --config ${CONFIG_FILE} --dry-run ${COMMAND}" >>/etc/crontabs/root

crond -f -d 8 -l 8