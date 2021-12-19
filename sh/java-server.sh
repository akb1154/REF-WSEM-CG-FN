#!/usr/bin/bash 

ip -4 -br -c address;
read -p "?q=ip" ip;
sudo java -jar ../jar/http.server.jar ip 443;