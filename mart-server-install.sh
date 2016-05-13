#! /bin/bash

# stop exist server
if systemctl status mart-server 2>&1 >/dev/null
then
	systemctl stop mart-server
fi

# copy server files
cp -Rf ./server /usr/local/mart

# make it a systemd service
cp -f ./mart-server.service /etc/systemd/system/
systemctl enable mart-server
systemctl start mart-server
