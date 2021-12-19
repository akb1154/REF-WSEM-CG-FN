package com.akb.sig.server;

import java.net.InetAddress;
import java.net.ServerSocket;

public class HTTPServer {

	private ServerSocket server;
	public static String ownIP; 

	public HTTPServer (String IP, int Port) throws Exception {
		ownIP = IP;
		server = new ServerSocket(Port, 2, InetAddress.getByName(IP));
		System.out.printf("Server accepting connection at %s%s\n", server.getInetAddress().toString().replace("/", ""), (Port == 80)? " " : ":"+Port);

		while (true) {
			new HTTPClient(server.accept());
		}
	}

	public static void main (String[] args) {
		try {
			Thread.currentThread().setName("server.main");
			new HTTPServer (args[0], 443);
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}
}
