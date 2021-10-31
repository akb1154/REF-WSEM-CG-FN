package com.akb.sig.server;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.util.HashMap;

public class HTTPClient extends Thread{

	private Socket con;
	private PrintStream ps;
	private BufferedReader bf;
	private HTTPRequest rq;
	
	public HTTPClient (Socket client) throws UnsupportedEncodingException, IOException {
		this.con = client;
		this.ps = new PrintStream (con.getOutputStream());
		this.bf = new BufferedReader (new java.io.InputStreamReader(con.getInputStream(), "utf-8"));
		this.start();
	}
	
	@Override
	public void run () {
		boolean isKA = reply();
		while (isKA) isKA = reply();
		try {
			con.close();
		} catch (IOException e) {
			e.printStackTrace();
		}		
	}
	
	private boolean reply () {
		int statusCode = 200;
		String File = "<html><head><title>ERROR！</title></head><body><h1>ERROR！</body><html>";
		
		try {
			this.rq = HTTPRequest.read(bf);
		}catch (IllegalArgumentException iae) {
			statusCode = 501;
		}catch (Exception ioe) {
			statusCode = 500;
		}
		
		if (statusCode == 200) {
			try {
				BufferedReader bfr = new BufferedReader (	new java.io.FileReader ( HTTPServer.HttpRoot + rq.getPath()));
				String tmpvar0 = "", tmpvar1 = "";
				while (tmpvar1 != null) { 
					tmpvar0 += tmpvar1;
					tmpvar1 = bfr.readLine();
				}
				File = tmpvar0;
			} catch (FileNotFoundException e) {
				statusCode = 404;
			} catch (Exception e) {
				statusCode = 500;
			}
		}	
		
		// Antwort
		String Response = "HTTP/1.1 %3d %s\r\n\r\n", status;
		status = (statusCode == 200)? "OK": (statusCode == 404)? "PNF" : (statusCode == 501)? "TODO_SUOMI" : "ERROR";
		ps.printf(Response, statusCode, status);
		ps.println(File);
		
		return (rq.getHeader("connection") == "keep-alive");
	}
	
}

