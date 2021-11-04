sudo ufw allow 442/tcp;
ip_address="";
ip address -4 -br -c | $ip_address;
echo $ip_address;
read -p "?q=BindAddress $" $ip_address;
java -jar ./jars/HTTPServer.jar 442 $ip_address;