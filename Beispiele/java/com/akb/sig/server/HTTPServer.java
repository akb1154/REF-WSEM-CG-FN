package com.akb.sig.server;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;

public class HTTPServer {

	public static final int HTTP_Standard = 80, Workaround = 442;
	
	protected static final String HttpRoot = "/home/akb1152/.http";
	private ServerSocket server;
	
	public static void main (String[] args) throws Exception {
		new HTTPServer(Integer.parseInt(args[0]), (args.length != 1)? args[1] : "");
	}
	
	/**
	 * Opens a <a href="https://docs.oracle.com/javase/8/docs/api/java/net/ServerSocket.html"> ServerSocket </a> on given Port 
	 * and binds to a given address 
	 * @see java.net.ServerSocket
	 * @param Port the port on which to open the Server (standard: 80)
	 * @param addr the address on which to bind the Server (standard: 127.0.0.1)
	 */
	public HTTPServer (int Port, String IP) throws IOException, Exception{
		if (Port <= 0) Port = HTTP_Standard; 
		if (IP.length() == 0) IP = "127.0.0.1";
		server = new ServerSocket(Port, 1, InetAddress.getByName(IP));
		while (true) {
			new HTTPClient (server.accept()); 			
		}	
	}
}
