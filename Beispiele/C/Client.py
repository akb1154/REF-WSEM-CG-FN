#!/usr/bin/env python3

import socket	

port = 80

s = socket.socket()	
host = "127.0.0.1"

s.connect((host, port))
s.send ("GET /index.html HTTP/1.1\r\nHost: 127.0.0.1\r\n\r\n".encode())

print (s.recv(2048).decode())  
s.close()
print ("done");