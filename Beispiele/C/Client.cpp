// Client side C/C++ program to demonstrate Socket programming
#include <stdio.h>
#include <sys/socket.h>
#include <arpa/inet.h>
#include <unistd.h>
#include <string.h>
#define PORT 443

int main(int argc, char const *argv[])
{
	int $socket = 0, valread;
	struct sockaddr_in serv_addr;
	char *request = "GET /index.html HTTP/1.1\nHost: 127.0.0.1\n\n";
	char buffer[1024] = {0};
	
	if (($socket = socket(AF_INET, SOCK_STREAM, 0)) < 0) {
		printf("\n Socket creation error \n");
		return -1;
	}

	serv_addr.sin_family = AF_INET;
	serv_addr.sin_port = htons(PORT);
	
	if(inet_pton(AF_INET, "127.0.0.1", &serv_addr.sin_addr)<=0)	{
		printf("\nInvalid address/ Address not supported \n");
		return -1;
	}

	if (connect($socket, (struct sockaddr *)&serv_addr, sizeof(serv_addr)) < 0) {
		printf("\nConnection Failed \n");
		return -1;
	}
	
	send($socket, request, strlen(request), 0);
	printf("Request sent\n");
	valread = read($socket, buffer, 1024);
	printf("%s\n", buffer);
	return 0;
}

