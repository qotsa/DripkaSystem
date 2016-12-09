#!/bin/sh

echo "$CRON /usr/bin/curator --config ${CONFIG_FILE} ${COMMAND}" >>/etc/crontabs/root
crond -f -d 8 -l 8